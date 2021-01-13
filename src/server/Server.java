package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Exception.FullServerPlayerException;

class Server {
	public static void main(String args[]) {
		ServerSocket main_sock = null;
		Account.initAccounts();
		
		try {
			main_sock = new ServerSocket(5656); // default port
			System.out.println("Running");
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		while (true) {
			try {
				Socket s = main_sock.accept(); // accept connection
				DataInputStream i = new DataInputStream(s.getInputStream());
				DataOutputStream o = new DataOutputStream(s.getOutputStream());
				(new Player(s, i, o)).start(); // start thread
			} catch (Exception e) {
				if(e instanceof FullServerPlayerException) {
					System.out.println("Full Player...");
				}
				else {
					System.out.println(e);
				}
			}
		}
		
	}
}