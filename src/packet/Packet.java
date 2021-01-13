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
	public static final byte SP_ROOM_INFO = 7;
	public static final byte SP_QUIT = 8;
	public static final byte SP_GAME_MOVE = 9;
	public static final byte SP_GAME_START = 10;
	public static final byte SP_GAME_WIN = 11;
	public static final byte SP_GET_MMR = 12;
	public static final byte SP_REGISTER = 13;
	public static final byte SP_TOP_RANK = 14;

	// Client packets tag
	public static final byte CP_LOGIN = 100;
	public static final byte CP_QUIT = 99;
	public static final byte CP_MESSAGE = 98;
	public static final byte CP_ROOM_OPT = 97;
	public static final byte CP_ROOM_LEAVE = 96;
	public static final byte CP_ROOM_JOIN = 95;
	public static final byte CP_GAME_START = 94;
	public static final byte CP_GAME_MOVE = 93;
	public static final byte CP_GAME_SURRENDER = 92;
	public static final byte CP_GET_MMR = 91;
	public static final byte CP_REGISTER = 90;
	public static final byte CP_TOP_RANK = 89;

	// Error Code
	public static final byte ERROR_FULL_PLAYER = 0;
	public static final byte ERROR_FULL_ROOM = 1;
	public static final byte ERROR_FULL_SEAT = 2;
	
	public static byte[] SPErrorPacket(byte ErrorCode) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_ERROR_PACKET); // write tag first
			p.writeByte(ErrorCode);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPYouAre(int id, String username, int mmr) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_YOU_ARE); // write tag first
			p.writeInt(id); // write fields
			p.writeUTF(username);
			p.writeInt(mmr);
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

	public static byte[] SPMessage(String message, int seat) {
		try {
			Packet p = New(20); // estimated size
			p.writeByte(SP_MESSAGE); // write tag first
			p.writeUTF(message);
			p.writeInt(seat);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPRoomPlayer(int roomId, int seat, int playerId, String userName) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_ROOM_PLAYER); // write tag first
			p.writeInt(roomId);
			p.writeInt(seat);
			p.writeInt(playerId);
			p.writeUTF(userName);
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

	public static byte[] SPGameStart(int boardSize) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_GAME_START); // write tag first
			p.writeInt(boardSize);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPGameWin(int seat) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_GAME_WIN); // write tag first
			p.writeInt(seat);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPGameMove(int x, int y, boolean isHorizontal, int seat) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_GAME_MOVE); // write tag first
			p.writeInt(x);
			p.writeInt(y);
			p.writeBoolean(isHorizontal);
			p.writeInt(seat);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPGetMmr(int mmr) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_GET_MMR); // write tag first
			p.writeInt(mmr);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPRegister(boolean success) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(SP_REGISTER); // write tag first
			p.writeBoolean(success);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] SPTopRank(String[] userNames, int[] mmrs) {
		try {
			Packet p = New(200); // estimated size
			p.writeByte(SP_TOP_RANK); // write tag first
			int userNumb = userNames.length;
			System.out.println(userNames[0]);
			p.writeInt(userNumb);
			for (int i = 0; i < userNumb; i++) {
				p.writeUTF(userNames[i]);
				p.writeInt(mmrs[i]);
			}
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

	public static byte[] CPMessage(String message) {
		try {
			Packet p = New(20); // estimated size
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

	public static byte[] CPGameStart(int boardSize) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_GAME_START); // write tag first
			p.writeInt(boardSize);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] CPGameSurrender() {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_GAME_SURRENDER); // write tag first
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] CPGameMove(int x, int y, boolean isHorizontal) {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_GAME_MOVE); // write tag first
			p.writeInt(x);
			p.writeInt(y);
			p.writeBoolean(isHorizontal);
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

	public static byte[] CPGetMmr() {
		try {
			Packet p = New(10); // estimated size
			p.writeByte(CP_GET_MMR); // write tag first
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] CPRegister(String username, String password) {
		try {
			Packet p = New(20); // estimated size
			p.writeByte(CP_REGISTER); // write tag first
			p.writeUTF(username);
			p.writeUTF(password);
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}

	public static byte[] CPTopRank() {
		try {
			Packet p = New(20); // estimated size
			p.writeByte(CP_TOP_RANK); // write tag first
			return p.buf(); // return the buffer
		} catch (IOException e) {
			return null;
		} // shouldn't happen
	}
}
