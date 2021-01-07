package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import packet.Packet;

public class Client implements Runnable {
	String userName;
	String passWord;
	Socket sock;
	DataInputStream in;
	DataOutputStream out;
	Thread listener;
	
	Controller controller;
	int myID = -1;
	String myName;
	private boolean connected = true;

	public boolean isConnected() {
		return connected;
	}

	Client(String u, String p, Socket s, DataInputStream i, DataOutputStream o, Controller controller) {
		this.controller = controller;
		userName = u;
		passWord = p;
		sock = s;
		in = i;
		out = o;
		(listener = new Thread(this)).start();
		output(Packet.CPLogin(userName, passWord));
	}

	void output(byte[] p) {
		if (p != null && p.length > 0) {
			try {
				out.write(p, 0, p.length);
			} catch (IOException e) {
				disconnect();
				this.stop();
			}
		}
	}

	public Integer working = Integer.valueOf(0);

	public void run() {
		loop: while (connected) {
			try {
				byte tag = in.readByte();
				synchronized (working) {
					switch (tag) {
					case Packet.SP_QUIT:
						break loop;
					case Packet.SP_YOU_ARE:
						hdYouAre();
						break;
					case Packet.SP_MESSAGE:
						hdMessage();
						break;
					case Packet.SP_LOGIN:
						hdLogin();
						break;
					case Packet.SP_ROOM_PLAYER:
						hdRoomPlayer();
						break;
					case Packet.SP_ERROR_PACKET:
						hdErrorPacket();
						break;
					case Packet.SP_LEAVE:
						hdLeave();
						break;
					case Packet.SP_GAME_START:
						hdGameStart();
						break;
					case Packet.SP_GAME_WIN:
						hdGameWin();
						break;
					case Packet.SP_GAME_MOVE:
						hdGameMove();
						break;
					}
					
				}

			} catch (IOException e) {
				e.printStackTrace();
				break loop;
			}
		}
		System.out.println("Close");
		this.stop();
		disconnect();
	}

	public void sendMessage(String message) {
		output(Packet.CPMessage(message));
	}

	void disconnect() {
		try {
			in.close();
			out.close();
			sock.close();
			connected = false;
			controller.disconnect();
		} catch (IOException e) {

		}
	}

	public void stop() {
		connected = false;
	}

	void hdYouAre() throws IOException {

		myID = in.readInt();
		myName = in.readUTF();
		System.out.println(myID);
		controller.loginSuccess(myID, myName);
	}
	
	void hdLeave() throws IOException {
		in.readInt();
	}

	void hdMessage() throws IOException {
		String message = in.readUTF();
		System.out.println(message);
	}

	void hdLogin() throws IOException {
		int tempId = in.readInt();
		if (tempId == -1) {
			output(Packet.CPQuit());
			disconnect();
			controller.loginFail();
		} else {
			myID = tempId;
			System.out.println(myID);
			controller.loginSuccess(myID, myName);
		}
	}
	
	void hdRoomPlayer() throws IOException {
		int roomId = in.readInt();
		int seat = in.readInt();
		int playerId = in.readInt();
		printPacket(Packet.SPRoomPlayer(roomId, seat, playerId));
		controller.roomPlayer(roomId, seat, playerId);
	}
	
	void hdGameStart() throws IOException {
		int boardSize = in.readInt();
		printPacket(Packet.SPGameStart(boardSize));
		controller.startGame(boardSize);
	}
	
	void hdErrorPacket() throws IOException {
		in.readUTF();
	}
	
	void hdGameWin() throws IOException {
		System.out.println("Hello");
		int seat = in.readInt();
		printPacket(Packet.SPGameWin(seat));
		
		controller.gameEnd(seat);
	}
	
	void hdGameMove() throws IOException {
		int x = in.readInt();
		int y = in.readInt();
		boolean isHorizontal = in.readBoolean();
		int seat = in.readInt();
		printPacket(Packet.SPGameMove(x, y, isHorizontal, seat));
		
		controller.processMove(x, y, isHorizontal, seat);
	}
	
	private void printPacket(byte[] p) {
		System.out.println("Recieved Packet:");
		System.out.printf("%2d ", p[0]);
		for (int i = 1; i < p.length; i++) {
			System.out.printf("%2x ", p[i]);
		}
		System.out.println();
	}

}
