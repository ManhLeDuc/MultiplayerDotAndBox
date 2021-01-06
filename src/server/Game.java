package server;

import game.Board;

public class Game {
	
	private Board currentBoard;
	
	public Game(int boardSize) {
		this.currentBoard = new Board(boardSize);
	}

}
