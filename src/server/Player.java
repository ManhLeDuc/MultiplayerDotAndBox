package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import packet.Packet;

class Player extends Thread {
	public static Player[] global = new Player[100];
	public int id = -1;
	public String name = null;
	private Socket sock;
	private DataInputStream in;
	private DataOutputStream out;
	public boolean connected = true;
	private PlayerOutput out_thread = null;
//	public Room table = null;
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
		output(Packet.SPYouAre(id)); // notify the player
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
				if (him != null && him.name != null)
					him.output(p);
			}
		}
	}

	public synchronized void disconnect() {
		if (id == -1)
			return; // we do it only once
		connected = false;
//		if (table != null) // leave the table
//			table.leave(this);
		synchronized (global) { // critical section
			global[id] = null; // will GC eventually
		}
		try { // close the socket
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
		}
		if (name != null) // announce leave
			outputAll(Packet.SPLeave(id));
		id = -1; // we're done here
	}

	private Integer working = Integer.valueOf(0);

	public void run() {

		loop: while (connected && !this.isInterrupted()) {
			try {
				byte tag = in.readByte();
				// don�t stop until we finish with this packet
				synchronized (working) {
					switch (tag) {
					case Packet.CP_QUIT:
						break loop;
//						case Packet.CP_MESSAGE:
//							hdMessage();
//							break;
					// call other packet handlers
					}
				}
			} catch (IOException e) {
				break loop;
			}

			connected = false;
			out_thread.interrupt();
			disconnect();
		}

	}

	public void stopOnWait() {
		connected = false; // so that run() won't loop again
		synchronized (working) { // stop only if blocked on input
			this.interrupt();
		}
	}

//	void hdMessage() throws IOException
//	{
//		// CP_MESSAGE contains a target type (MSG_SERVER,
//		// MSG_TABLE, or MSG_ALL), and a text message.
//		byte type = in.readByte();
//		String text = in.readUTF();
//		if (type == Packet.MSG_SERVER)
//			System.out.println("Msg from " + id + ": " + text);
//		else if (type == Packet.MSG_TABLE && table != null)
//			table.outputTable(Packet.SPMessage(Packet.MSG_TABLE, id, text));
//		else
//			outputAll(Packet.SPMessage(Packet.MSG_ALL, id, text));
//	}
	
	void hdLogin() throws IOException
	{
		// CP_MESSAGE contains a target type (MSG_SERVER,
		// MSG_TABLE, or MSG_ALL), and a text message.
		String username = in.readUTF();
		String password = in.readUTF();
	}

}