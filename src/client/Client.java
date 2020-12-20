package client;

import java.io.*;
import java.net.*;

import packet.Packet;

public class Client implements Runnable {
	private boolean exit;
	String userName;
	String passWord;
	Socket sock;
	DataInputStream in;
	DataOutputStream out;
	Thread listener;
	int myID = -1;
	public boolean connected = true;

	Client(String u, String p, Socket s, DataInputStream i, DataOutputStream o) {
		userName = u;
		passWord = p;
		sock = s;
		in = i;
		out = o;
		exit = false;
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

	public void run() {
		loop: while (connected&&exit) {
			try {
				byte tag = in.readByte();
				switch (tag) {
				case Packet.SP_LEAVE:
					break loop;
				case Packet.SP_YOU_ARE:
					hdYouAre();
					break;
//						case Packet.CP_MESSAGE:
//							hdMessage();
//							break;
				// call other packet handlers
				}
			} catch (IOException e) {
				break loop;
			}

			connected = false;
			this.stop();
			disconnect();
		}
	}

	void disconnect() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
		}
	}

	public void stop() {
		exit = true;
	}
	
	

}
