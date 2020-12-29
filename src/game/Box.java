package game;

public class Box {
	private Edge hTEdge;
	private Edge hDEdge;
	private Edge vREdge;
	private Edge vLEdge;
	private int trangthai;

	public Box(Edge hTEdge, Edge hDEdge, Edge vREdge, Edge vLEdge, int trangthai) {
		super();		
		this.hTEdge = hTEdge;
		this.hDEdge = hDEdge;
		this.vREdge = vREdge;
		this.vLEdge = vLEdge;
		this.trangthai = trangthai;
	}
	
	public Box(Box box) {
		this.hDEdge = box.gethDEdge();
		this.hTEdge = box.gethTEdge();
		this.vLEdge = box.getvLEdge();
		this.vREdge = box.getvREdge();
		this.trangthai = box.getTrangthai();
	}

	public Edge gethTEdge() {
		return hTEdge;
	}

	public void sethTEdge(Edge hTEdge) {
		this.hTEdge = hTEdge;
	}

	public Edge gethDEdge() {
		return hDEdge;
	}

	public void sethDEdge(Edge hDEdge) {
		this.hDEdge = hDEdge;
	}

	public Edge getvREdge() {
		return vREdge;
	}

	public void setvREdge(Edge vREdge) {
		this.vREdge = vREdge;
	}

	public Edge getvLEdge() {
		return vLEdge;
	}

	public void setvLEdge(Edge vLEdge) {
		this.vLEdge = vLEdge;
	}

	public int getTrangthai() {
		return trangthai;
	}

	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}

	public Box() {
		this.trangthai = ColorTeam.BLANK;
	}
	
	public boolean checkBox() {
		if( hTEdge.getColorOfEdge() == ColorTeam.BLACK && hDEdge.getColorOfEdge() == ColorTeam.BLACK 
				&& vREdge.getColorOfEdge() == ColorTeam.BLACK && vLEdge.getColorOfEdge() == ColorTeam.BLACK) 
			return true;
		return false;
	}
	public int sethEdge(int x, int y, int color) {
		if(hDEdge.updateColor(x, y) == false && hTEdge.updateColor(x, y) == false)
			return 0;
		if(checkBox() == true)
		{
			this.trangthai = color;
			return 2;
		}
		return 1;
	}
	
	public int setvEdge(int x, int y, int color) {
		if(vLEdge.updateColor(x, y) == false && vREdge.updateColor(x, y) == false)
			return 0;
		if(checkBox() == true)
		{
			this.trangthai = color;
			return 2;
		}
		return 1;
	}
	
	public int countEdgeBlack() {
		int count = 0;
		if(hTEdge.getColorOfEdge() == ColorTeam.BLACK) count++;
		if(hDEdge.getColorOfEdge() == ColorTeam.BLACK) count++;
		if(vREdge.getColorOfEdge() == ColorTeam.BLACK) count++;
		if(vLEdge.getColorOfEdge() == ColorTeam.BLACK) count++;
		return count;
	}

}
