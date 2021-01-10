package server;

import packet.Packet;

public class Room {
	public static Room[] global = new Room[50];
	int id = -1;
	private Player[] players = new Player[2];

	private ServerGame currentGame = null;

	public Player[] getPlayers() {
		return players;
	}

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
					Player.outputAll(Packet.SPRoomPlayer(id, i, him.id, him.getAccount().getUserName()));
					break join;
				}
			}
			him.output(Packet.SPErrorPacket("Table is full."));
			return;
		}
	}

	public void leave(Player him) {

		synchronized (global) { // critical section
			synchronized (this) {
				if (this.currentGame != null) {
					int winnerSeat = (him.seat == 0 ? 1 : 0);
					this.endGame(winnerSeat);
				}

				if (him == null) {
					return;
				}
				if (him.room != this)
					return;
				// announce to all; player id of -1 means leaving table
				Player.outputAll(Packet.SPRoomPlayer(id, him.seat, -1, him.getAccount().getUserName()));
				if (him.seat == 0) {
					Player otherPlayer = him.room.players[1];
					this.leave(otherPlayer);
				}
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
				him.output(Packet.SPRoomPlayer(id, i, this.players[i].id, him.getAccount().getUserName()));
		}
	}

	public synchronized void startGame(int boardSize) {

		for (int i = 0; i < players.length; i++)
			if (players[i] == null)
				return;
		if (this.currentGame != null) {
			return;
		}
		this.currentGame = new ServerGame(boardSize, this);
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null)
				players[i].output(Packet.SPGameStart(boardSize));
		}

	}

	public synchronized void endGame(int seat) {
		if(this.currentGame == null) {
			return;
		}
		
		for (int i = 0; i < players.length; i++)
			if (players[i] == null)
				return;
		
		Elo.EloRating(players[0].getAccount(), players[1].getAccount(), seat);
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].output(Packet.SPGameWin(seat));
			}
		}
		this.currentGame = null;

	}
	
	public synchronized void sendMessage(String text, int seat) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				players[i].output(Packet.SPMessage(text, seat));
			}
		}
	}

	public void processMove(int x, int y, boolean isHorizontal, int seat) {
		synchronized (this) {
			if (this.currentGame != null) {
				this.currentGame.processMove(x, y, isHorizontal, seat);
			}
		}
	}
}