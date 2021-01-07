package game;
public class Edge {

	private int x, y;
	private boolean horizontal;
	private int colorOfEdge;
	public Edge( ) {
		x = y = -1;
		horizontal = false;
		colorOfEdge = ColorTeam.BLANK;
	}

	public Edge(int x, int y, boolean horizontal) {
		this.x = x;
		this.y = y;
		this.horizontal = horizontal;
		this.colorOfEdge = ColorTeam.BLANK;
	}
	
	public Edge(Edge edge) {
		this.x = edge.getX();
		this.y = edge.getY();
		this.horizontal = edge.isHorizontal();
		this.colorOfEdge = edge.getColorOfEdge();
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return ((horizontal ? "H " : "V ") + x + " " + y);
	}

	public int getColorOfEdge() {
		return colorOfEdge;
	}

	public void setColorOfEdge(int colorOfEdge) {
		this.colorOfEdge = colorOfEdge;
	}
	
	public boolean updateColor(int x, int y) {
		if(this.x == x && this.y == y && this.colorOfEdge == ColorTeam.BLANK)
			{
				this.colorOfEdge = ColorTeam.BLACK;
				return true;
			}
		return false;
	}
	

}
