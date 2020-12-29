package client.solver;
import java.util.ArrayList;
import java.util.Random;

import game.Board;
import game.Edge;

public class RandomSolver extends GameSolver {

    @Override
    public Edge getNextMove(final Board board, int color) {
        ArrayList<Edge> moves = board.getAvailableMoves();
        return moves.get(new Random().nextInt(moves.size()));
    }

}
