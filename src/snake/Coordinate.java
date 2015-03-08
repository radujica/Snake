package snake;


public class Coordinate {

	private int x,
				y;
	
	public Coordinate(){
		x=0;
		y=0;
	}
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public Coordinate increaseX(int value){
		x +=value;
		return this;
	}
	public Coordinate increaseY(int value){
		y += value;
		return this;
	}
	public Coordinate decreaseX(int value){
		x -= value;
		return this;
	}
	public Coordinate decreaseY(int value){
		y -= value;
		return this;
	}
	public void resetX(int value){
		this.x = value;
	}
	public void resetY(int value){
		this.y = value;
	}
	boolean equals(Coordinate c) {
		return c.x == x && c.y == y;
	}
	public Coordinate copy() {
		return new Coordinate(x,y);
	}
}
