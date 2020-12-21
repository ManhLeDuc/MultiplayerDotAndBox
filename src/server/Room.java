package server;

import packet.Packet;

public class Room {
	public static Room[] global = new Room[50];
	int id = -1;
	Player[] players = new Player[2];

	public Room() throws FullServerException {
		// assign a unique table id
		synchronized (global) { // critical section
			for (int i = 0; i < global.length; i++) {
				if (global[i] == null) {
					id = i;
					global[i] = this;
					break;
				}
			}
		}
		if (id == -1) {
			throw new FullServerException();
		}
			
	}

	public synchronized void outputTable(byte[] p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null)
				players[i].output(p);
		}
	}

	public synchronized void join(Player him) {
		join: {
			for (int i = 0; i < players.length; i++) {
				if (players[i] == null) {
					players[i] = him;
					him.room = this;
					him.seat = i;
					Player.outputAll(Packet.SPRoomPlayer(id, i, him.id));
					update(him);
					break join;
				}
			}
			him.output(Packet.SPErrorPacket("Table is full."));
			return;
		}
		// MahJong-related stuff here
	}

	public void leave(Player him) {
		synchronized (global) { // critical section
			synchronized (this) {
				System.out.println("Start Leave");
				if (him.room != this)
					return;
				// announce to all; player id of -1 means leaving table
				Player.outputAll(Packet.SPRoomPlayer(id, him.seat, -1));
				players[him.seat] = null;
				him.room = null;
				him.seat = -1;
				for (int i = 0; i < players.length; i++)
					if (players[i] != null)
						return;
				// now the table has only spectators, so we close it
				global[id] = null;
			}
		}
	}

	public synchronized void update(Player him) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null)
				him.output(Packet.SPRoomPlayer(id, i, this.players[i].id));
		}
	}
	// other methods...
}