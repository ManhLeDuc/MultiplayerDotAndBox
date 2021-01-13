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
	int myMmr = -1;
	private boolean connected = true;

	public boolean isConnected() {
		return connected;
	}
	//Login
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
	
	//Register
	Client(Socket s, DataInputStream i, DataOutputStream o, Controller controller) {
		this.controller = controller;
		sock = s;
		in = i;
		out = o;
		(listener = new Thread(this)).start();
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
					case Packet.SP_GET_MMR:
						hdGetMmr();
						break;
					case Packet.SP_REGISTER:
						hdRegister();
						break;
					case Packet.SP_TOP_RANK:
						hdTopRank();
						break;
					}
					
				}

			} catch (IOException e) {
//				e.printStackTrace();
				break loop;
			}
		}
		System.out.println("Disconnected");
		this.stop();
		disconnect();
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
		myMmr = in.readInt();
		printPacket(Packet.SPYouAre(myID, myName, myMmr));
		controller.loginSuccess(myID, myName, myMmr);
	}
	
	void hdLeave() throws IOException {
		in.readInt();
	}

	void hdMessage() throws IOException {
		String message = in.readUTF();
		int seat = in.readInt();
		printPacket(Packet.SPMessage(message, seat));
		this.controller.handleMessage(message, seat);
	}

	void hdLogin() throws IOException {
		int tempId = in.readInt();
		printPacket(Packet.SPLogin(tempId));
		if (tempId == -1) {
			output(Packet.CPQuit());
			disconnect();
			controller.loginFail();
		}
	}
	
	void hdRoomPlayer() throws IOException {
		int roomId = in.readInt();
		int seat = in.readInt();
		int playerId = in.readInt();
		String otherUserName = in.readUTF();
		printPacket(Packet.SPRoomPlayer(roomId, seat, playerId, otherUserName));
		controller.roomPlayer(roomId, seat, playerId, otherUserName);
	}
	
	void hdGameStart() throws IOException {
		int boardSize = in.readInt();
		printPacket(Packet.SPGameStart(boardSize));
		controller.startGame(boardSize);
	}
	
	void hdErrorPacket() throws IOException {
		byte errorCode = in.readByte();
		printPacket(Packet.SPErrorPacket(errorCode));
		controller.handleError(errorCode);
	}
	
	void hdGameWin() throws IOException {
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
	
	void hdGetMmr() throws IOException {
		int mmr = in.readInt();
		printPacket(Packet.SPGetMmr(mmr));
		this.myMmr = mmr;
		controller.processMmr(mmr);
	}
	
	void hdRegister() throws IOException {
		boolean success = in.readBoolean();
		printPacket(Packet.SPRegister(success));
		controller.handleRegister(success);
		this.disconnect();
	}
	
	void hdTopRank() throws IOException {
		int numb = in.readInt();
		String[] userNames = new String[numb];
		int[] mmrs = new int[numb];
		for(int i =0; i<numb; i++) {
			userNames[i] = in.readUTF();
			mmrs[i] = in.readInt();
		}
		controller.handleTopRank(userNames, mmrs);
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
