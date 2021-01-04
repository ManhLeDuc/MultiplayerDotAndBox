package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import packet.Packet;

class Player extends Thread {
	public static Player[] global = new Player[100];
	public int id = -1;
	private Account account = null;
	private Socket sock;
	private DataInputStream in;
	private DataOutputStream out;
	public boolean connected = true;
	private PlayerOutput out_thread = null;
	public Room room = null;
	public int seat = -1;

	public Player(Socket s, DataInputStream in, DataOutputStream out) throws FullServerException {
		synchronized (global) { // find a unique player id
			for (int i = 0; i < global.length; i++) {
				if (global[i] == null) {
					id = i;
					global[i] = this;
					break;
				}
			}
		}
		if (id == -1) // all slots full
			throw new FullServerException();
		sock = s;
		this.in = in;
		this.out = out;
		(out_thread = new PlayerOutput(this, out)).start();
		System.out.println("New Client connected");
	}

	public void output(byte[] p) {
		if (connected)
			out_thread.output(p);
	}

	public static void outputAll(byte[] p) {
		synchronized (global) { // critical section
			for (int i = 0; i < global.length; i++) {
				Player him = global[i];
				// only send to players who have logged in
				if (him != null)
					him.output(p);
			}
		}
	}

	public synchronized void disconnect() {
		if (id == -1)
			return; // we do it only once
		connected = false;
		if (room != null) // leave the table
			room.leave(this);
		if (account != null) {
			Account.logout(account.getUserName());
		}
		synchronized (global) { // critical section
			global[id] = null; // will GC eventually
		}
		try { // close the socket
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
		}
		outputAll(Packet.SPLeave(id));
		id = -1; // we're done here
	}

	private Integer working = Integer.valueOf(0);

	public void run() {

		loop: while (connected && !this.isInterrupted()) {
			try {
				byte tag = in.readByte();
				// don’t stop until we finish with this packet
				synchronized (working) {
					switch (tag) {
					case Packet.CP_QUIT:
						break loop;
					case Packet.CP_LOGIN:
						hdLogin();
						break;
					case Packet.CP_MESSAGE:
						hdMessage();
						break;
					case Packet.CP_ROOM_OPT:
						hdRoomOpt();
						break;
					case Packet.CP_ROOM_LEAVE:
						hdRoomLeave();
						break;
					case Packet.CP_ROOM_JOIN:
						hdRoomJoin();
						break;
					// call other packet handlers
					}
				}
			} catch (IOException e) {

				break loop;
			}
		}
		connected = false;
		out_thread.interrupt();
		disconnect();

	}

	public void stopOnWait() {
		connected = false; // so that run() won't loop again
		synchronized (working) { // stop only if blocked on input
			this.interrupt();
		}
	}

	void hdMessage() throws IOException {

		String text = in.readUTF();
		output(Packet.SPMessage(text));
	}

	void hdLogin() throws IOException {

		String username = in.readUTF();
		String password = in.readUTF();
		printPacket(Packet.CPLogin(username, password));
		
		if ((account = Account.login(username, password)) != null) {
			output(Packet.SPLogin(id));
			synchronized (Room.global) {
				for (int i = 0; i < Room.global.length; i++) {
					if (Room.global[i] != null) {
						Room.global[i].update(this);
					}
				}
			}
		} else {
			output(Packet.SPLogin(-1));
		}
	}

	void hdRoomOpt() throws IOException {
		printPacket(Packet.CPRoomOpt());
		if (this.room == null) {
			try {
				this.room = new Room();
				this.room.join(this);
			} catch (Exception e) {

			}
		}

	}

	void hdRoomLeave() throws IOException {
		printPacket(Packet.CPRoomLeave());
		if (this.room != null) {
			this.room.leave(this);
		}
	}

	void hdRoomJoin() throws IOException {
		int roomId = in.readInt();
		printPacket(Packet.CPRoomJoin(roomId));
		if (this.room == null) {
			try {
				Room.global[roomId].join(this);
			} catch (Exception e) {

			}
		}
	}
	
	private void printPacket(byte[] p) {
		System.out.println("Recieved Packet:");
		for (int i = 0; i < p.length; i++) {
			System.out.printf("%2x ", p[i]);
		}
		System.out.println();
	}
}