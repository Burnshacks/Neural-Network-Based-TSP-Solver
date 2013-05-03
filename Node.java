package TSP;

public class Node {
	
	int id = 0;
	int x = 0;
	int y = 0;
	
	public Node(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	
	// Trivial methods
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getX() {
		return x;
	}



	public void setX(int x) {
		this.x = x;
	}



	public int getY() {
		return y;
	}



	public void setY(int y) {
		this.y = y;
	}



	@Override
	public String toString() {
		return "Node [id=" + id + ", x=" + x + ", y=" + y + "]";
	}
	
	
	
}
