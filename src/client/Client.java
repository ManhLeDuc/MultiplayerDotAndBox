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
//	LoginGUI loginGUI = null;
	TestGUI loginGUI = null;
	RoomListGUI roomGUI = null;
	int myID = -1;
	private boolean connected = true;

	public boolean isConnected() {
		return connected;
	}

//	Client(String u, String p, Socket s, DataInputStream i, DataOutputStream o, LoginGUI loginGUI) {
//		this.loginGUI = loginGUI;
//		userName = u;
//		passWord = p;
//		sock = s;
//		in = i;
//		out = o;
//		(listener = new Thread(this)).start();
//		output(Packet.CPLogin(userName, passWord));
//
//	}
	
	Client(String u, String p, Socket s, DataInputStream i, DataOutputStream o, TestGUI loginGUI) {
		this.loginGUI = loginGUI;
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
					case Packet.SP_LEAVE:
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
//							case Packet.CP_MESSAGE:
//								hdMessage();
//								break;
					// call other packet handlers
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
//			if(roomGUI!=null) {
//				roomGUI.networkFail();
//			}
			loginGUI.loginFail();
		} catch (IOException e) {

		}
	}

	public void stop() {
		connected = false;
	}

	void hdYouAre() throws IOException {

		myID = in.readInt();
		System.out.println(myID);
		loginGUI.loginSuccess();
	}

	void hdMessage() throws IOException {
		String message = in.readUTF();
		System.out.println(message);
	}
	
	void hdLogin() throws IOException {
		String message = in.readUTF();
		if(message.equals("ngu")) {
			output(Packet.CPQuit());
			disconnect();
			loginGUI.loginFail();
		}
	}

}
