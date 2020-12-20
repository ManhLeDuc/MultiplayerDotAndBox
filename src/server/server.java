package server;

import java.net.*;
import java.io.*;

class Server {
	public static void main(String args[]) {
		ServerSocket main_sock = null;
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
				(new Player(s, i, o)).start(); // start thread
			} catch (Exception e) {
				System.out.println();
			}
		}
	}
}