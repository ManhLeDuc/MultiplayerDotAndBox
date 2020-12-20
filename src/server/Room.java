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
		if (id == -1)
			throw new FullServerException();
	}

	public synchronized void outputTable(byte[] p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null)
				players[i].output(p);
		}
	}

//	public synchronized void join(Player him) {
//		join: {
//			// seats 0 - 3 are for players, the rest spectators
//			for (int i = 0; i < players.length; i++) {
//				if (players[i] == null) {
//					players[i] = him;
//					him.table = this;
//					him.seat = i;
//					Player.outputAll(Packet.SPTablePlayer(id, i, him.id));
//					break join;
//				}
//			}
//			him.output(Packet.SPMessage(Packet.MSG_GOD, 0, "Table is full."));
//			return;
//		}
//		// MahJong-related stuff here
//	}

//	public void leave(Player him) {
//		synchronized (global) { // critical section
//			synchronized (this) {
//				if (him.table != this)
//					return;
//				// announce to all; player id of -1 means leaving table
//				Player.outputAll(Packet.SPTablePlayer(id, him.seat, -1));
//				players[him.seat] = null;
//				him.table = null;
//				him.seat = -1;
//				for (int i = 0; i < 4; i++)
//					if (players[i] != null)
//						return;
//				// now the table has only spectators, so we close it
//				for (int i = 4; i < players.length; i++) {
//					if (players[i] != null) {
//						Player.outputAll(Packets.SPTablePlayer(id, i, -1));
//						players[i].table = null;
//						players[i].seat = -1;
//						players[i] = null;
//					}
//				}
//				global[id] = null;
//			}
//		}
//	}

//	public synchronized void update(Player him) {
//		for (int i = 0; i < players.length; i++) {
//			if (players[i] != null)
//				him.output(Packet.SPTablePlayer(id, i, t.players[i].id));
//		}
//	}
	// other methods...
}