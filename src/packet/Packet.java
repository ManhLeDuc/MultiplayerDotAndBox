package packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	public static final byte SP_ERROR_PACKET = 0;
	public static final byte SP_YOU_ARE = 1;
	public static final byte SP_LOGIN = 2;
	public static final byte SP_LEAVE = 3;
	public static final byte SP_MESSAGE = 4;
	public static final byte SP_ROOM_OPT = 5;
	public static final byte SP_ROOM_PLAYER = 6;

	// Client packets tag
	public static final byte CP_LOGIN = 100;
	public static final byte CP_QUIT = 99;
	public static final byte CP_MESSAGE = 98;
	public static final byte CP_ROOM_OPT = 97;
	public static final byte CP_ROOM_LEAVE = 96;
	public static final byte CP_ROOM_JOIN = 95;

	public static byte[] SPErrorPacket(String message) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_ERROR_PACKET); // write tag first
			p.writeUTF(message);
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
	
	public static byte[] SPLogin(int playerId) {
		try {
			Packet p = New(20); // estimated size
			p.writeByte(SP_LOGIN); // write tag first
			p.writeInt(playerId);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] SPMessage(String message) {
		try {
			Packet p = New(20); // estimated size
			p.writeByte(SP_MESSAGE); // write tag first
			p.writeUTF(message);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] CPMessage(String message) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_MESSAGE); // write tag first
			p.writeUTF(message);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] CPQuit() {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_QUIT); // write tag first
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] SPRoomPlayer(int roomId, int seat, int playerId) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_ROOM_PLAYER); // write tag first
			p.writeInt(roomId);
			p.writeInt(seat);
			p.writeInt(playerId);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] SPRoomOpt(int roomId) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_ROOM_OPT); // write tag first
			p.writeInt(roomId);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] CPRoomOpt() {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_ROOM_OPT); // write tag first
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] CPRoomJoin(int roomId) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_ROOM_JOIN); // write tag first
			p.writeInt(roomId);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
	
	public static byte[] CPRoomLeave() {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_ROOM_LEAVE); // write tag first
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
}
