import java.awt.Graphics;

public class MapObject {
	protected int x;
	protected int y;
	protected final boolean canCollide;
	public MapObject(int x, int y, boolean canCollide) {
		this.x = x;
		this.y = y;
		this.canCollide = canCollide;
	}
	public void paint(Graphics g) {
		
	}
}
