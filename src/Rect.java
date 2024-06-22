
public class Rect {
	public double x;
	public double y;
	public double width;
	public double height;
	public Rect() {
		x = 0;
		y = 0; 
		width = 0;
		height = 0;
	}
	public Rect(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public double getMaxX() {
		return x + width;
	}
	public double getMaxY() {
		return y + height;
	}
	public double getCenterX() {
		return x + width/2;
	}
	public double getCenterY() {
		return y + height/2;
	}
	public boolean intersects(Rect rect) {
		return ((x >= rect.x && x <= rect.getMaxX()) && (y >= rect.y && y <= rect.getMaxY())) || ((getMaxX() >= rect.x && getMaxX() <= rect.getMaxX()) && (getMaxY() >= rect.y && getMaxY() <= rect.getMaxY())) || ((rect.getMaxX() >= x && rect.getMaxX() <= getMaxX()) && (rect.y >= y && rect.y <= getMaxY())) || ((rect.x >= x && rect.x <= getMaxX()) && (rect.getMaxY() >= y && rect.getMaxY() <= getMaxY()));
	}
}
