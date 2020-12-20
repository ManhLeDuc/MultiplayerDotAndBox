package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

class Server {
	public static void main(String args[]) {
		ServerSocket main_sock = null;
		HashMap<String, String> accounts = new HashMap<String, String>();
		accounts.put("Test","123123");
		accounts.put("Test1","123123");
		accounts.put("Test2","123123");
		accounts.put("Test3","123123");
		try {
			main_sock = new ServerSocket(5656); // default port
			System.out.println("Running");
		} catch (IOException e) {
			System.out.println();
			System.exit(1);
		}
		while (true) {
			try {
				Socket s = main_sock.accept(); // accept connection
				DataInputStream i = new DataInputStream(s.getInputStream());
				DataOutputStream o = new DataOutputStream(s.getOutputStream());
				(new Player(s, i, o, accounts)).start(); // start thread
			} catch (Exception e) {
				System.out.println();
			}
		}
		
	}
}