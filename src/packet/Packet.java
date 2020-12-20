package packet;

import java.io.*;

public class Packet extends DataOutputStream {
	ByteArrayOutputStream b_out; // holds binary data

	private Packet(ByteArrayOutputStream buf) {
		super(buf);
		b_out = buf;
	}

	private byte[] buf() {
		return b_out.toByteArray();
	}

	private static Packet New(int size) { // make a new packet
		ByteArrayOutputStream buf = new ByteArrayOutputStream(size);
		return new Packet(buf);
	}

	// Server packets tag
	public static final byte SP_SOME_PACKET = 0;
	public static final byte SP_YOU_ARE = 1;
	public static final byte SP_LOGIN = 2;
	public static final byte SP_LEAVE = 3;
	public static final byte SP_MESSAGE = 4;

	// Client packets tag
	public static final byte CP_LOGIN = 100;
	public static final byte CP_QUIT = 99;
	public static final byte CP_MESSAGE = 98;

	public static byte[] SPSomePacket(byte b, int i, String s) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_SOME_PACKET); // write tag first
			p.writeByte(b); // write fields
			p.writeInt(i);
			p.writeUTF(s);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPYouAre(int id) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_YOU_ARE); // write tag first
			p.writeInt(id); // write fields
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] SPLeave(int id) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_LEAVE); // write tag first
			p.writeInt(id); // write fields
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] CPLogin(String userName, String passWord) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_LOGIN); // write tag first
			p.writeUTF(userName);
			p.writeUTF(passWord);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] SPLogin(String result) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_LOGIN); // write tag first
			p.writeUTF(result);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
}
