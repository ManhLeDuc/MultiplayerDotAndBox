package game;

import java.awt.Point;
import java.util.ArrayList;

public class Board implements Cloneable {

	private ArrayList<Edge> hEdges;
	private ArrayList<Edge> vEdges;
	private ArrayList<Box> box;
	private int n, redScore, blueScore;

	public Board(int n) {
		int count = 0;
		hEdges = new ArrayList<Edge>();
		vEdges = new ArrayList<Edge>();
		box = new ArrayList<Box>();

		for (int i = 0; i < (n - 1) * (n - 1); i++) {
			box.add(new Box());
		}

		for (int i = 0; i < (n - 1); i++) {
			for (int j = 0; j < n; j++) {
				hEdges.add(new Edge(i, j, true));
				count++;
				if (j == 0) {
					box.get(i).sethTEdge(hEdges.get(count - 1));
				} else if (j < n - 1) {
					box.get(i + (j - 1) * (n - 1)).sethDEdge(hEdges.get(count - 1));
					box.get(i + j * (n - 1)).sethTEdge(hEdges.get(count - 1));
				} else {
					box.get(i + (j - 1) * (n - 1)).sethDEdge(hEdges.get(count - 1));
				}

			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < (n - 1); j++) {
				Edge v = new Edge(i, j, false);
				vEdges.add(v);
				if (i == 0)
					box.get(j * (n - 1)).setvLEdge(v);
				else if (i < (n - 1)) {
					box.get(i - 1 + j * (n - 1)).setvREdge(v);
					box.get(i + j * (n - 1)).setvLEdge(v);
				} else {
					box.get(i - 1 + j * (n - 1)).setvREdge(v);
				}
			}
		}
		this.n = n;
		redScore = blueScore = 0;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public int getSize() {
		return n;
	}

	public int getRedScore() {
		return redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public int getScore(int color) {
		if (color == ColorTeam.RED)
			return redScore;
		else
			return blueScore;
	}

	public static int toggleColor(int color) {
		if (color == ColorTeam.RED)
			return ColorTeam.BLUE;
		else
			return ColorTeam.RED;
	}

	public ArrayList<Point> setHEdge(int x, int y, int color) {
		ArrayList<Point> ret = new ArrayList<Point>();
		int numb = x + y * (n - 1);
		int i = 0;
		int fail = 0;
		if (y < (n - 1)) {
			i = box.get(numb).sethEdge(x, y, color);
			if (i == 2) {
				ret.add(new Point(x, y));
				if (color == ColorTeam.RED)
					redScore++;
				else
					blueScore++;
			} else if (i == 0) {
				fail += 1;
			}
		}
		if (y > 0) {
			i = box.get(numb - n + 1).sethEdge(x, y, color);
			if (i == 2) {
				ret.add(new Point(x, y - 1));
				if (color == ColorTeam.RED)
					redScore++;
				else
					blueScore++;
			} else if (i == 0) {
				fail += 1;
			}
		}
		if(fail==2)
			return null;
		return ret;
	}

	public ArrayList<Point> setVEdge(int x, int y, int color) {
		ArrayList<Point> ret = new ArrayList<Point>();
		
		int numb = x + y * (n - 1);
		int i = 0;
		int fail = 0;
		if (x < (n - 1)) {
			i = box.get(numb).setvEdge(x, y, color);
			if (i == 2) {
				ret.add(new Point(x, y));
				if (color == ColorTeam.RED)
					redScore++;
				else
					blueScore++;
			} else if (i == 0) {
				fail += 1;
			}

		}
		if (x > 0) {
			i = box.get(numb - 1).setvEdge(x, y, color);
			if (i == 2) {
				ret.add(new Point(x - 1, y));
				if (color == ColorTeam.RED)
					redScore++;
				else
					blueScore++;
			} else if (i == 0) {
				fail += 1;
			}
		}
		if(fail==2)
			return null;
		return ret;
	}

	public boolean isComplete() {
		return (redScore + blueScore) == (n - 1) * (n - 1);
	}

	public int getWinner() {
		if (redScore > blueScore)
			return ColorTeam.RED;
		else if (redScore < blueScore)
			return ColorTeam.BLUE;
		else
			return ColorTeam.BLANK;
	}

	public void clear() {
		box.clear();
		hEdges.clear();
		vEdges.clear();
		redScore = 0;
		blueScore = 0;
	}

}
