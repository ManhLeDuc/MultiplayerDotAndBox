package server;

import java.awt.Point;
import java.util.ArrayList;

import game.Board;
import game.ColorTeam;
import packet.Packet;

public class ServerGame {

	private int turn;
	private Room currentRoom;
	private Board currentBoard;

	public ServerGame(int boardSize, Room currentRoom) {
		this.currentBoard = new Board(boardSize);
		this.turn = ColorTeam.RED;
		this.currentRoom = currentRoom;

	}

	public void processMove(int x, int y, boolean isHorizontal, int seat) {
		if (turn != seat) {
			return;
		}
		ArrayList<Point> ret;
		if (isHorizontal) {
			ret = this.currentBoard.setHEdge(x, y, seat);
			System.out.println("Set H Edge");
			if (ret != null) {
				if (ret.size() == 0) {
					this.turn = (seat == 0 ? 1 : 0);
				}
				for (int i = 0; i < this.currentRoom.getPlayers().length; i++) {
					this.currentRoom.getPlayers()[i].output(Packet.SPGameMove(x, y, isHorizontal, seat));
				}
			}
			else {
				System.out.println("Set H Edge Failed");
			}
		} else {
			ret = this.currentBoard.setVEdge(x, y, seat);
			System.out.println("Set V Edge");
			if (ret != null) {
				if (ret.size() == 0) {
					this.turn = (seat == 0 ? 1 : 0);
				}
				for (int i = 0; i < this.currentRoom.getPlayers().length; i++) {
					this.currentRoom.getPlayers()[i].output(Packet.SPGameMove(x, y, isHorizontal, seat));
				}
			}
			else {
				System.out.println("Set V Edge Failed");
			}
		}

		if (this.currentBoard.isComplete()) {
			int winner = currentBoard.getWinner();
			if (winner == ColorTeam.RED) {
				gameWinner(winner);
			} else if (winner == ColorTeam.BLUE) {
				gameWinner(winner);
			} else {
				for (int i = 0; i < this.currentRoom.getPlayers().length; i++) {
					gameWinner(-1);
				}
			}
			
			
		}
	}
	
	private void gameWinner(int seat) {
		this.currentRoom.endGame(seat);
	}

}
