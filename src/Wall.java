import java.awt.Color;
import java.awt.Graphics;

public class Wall extends MapObject {

	public Wall(int x, int y) {
		super(x, y, true);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(x * 28, y * 35, 28, 35);
		g.setColor(Color.black);
		try {
			if (!PoliceChase.grid[y - 1][x].canCollide){
				g.fillRect(x * 28, y * 35, 28, 3);
			}
		} catch (Exception e) {
			g.fillRect(x * 28, y * 35, 28, 3);
		}
		try {
			if (!PoliceChase.grid[y - 1][x + 1].canCollide){
				g.fillRect(x * 28 + 25, y * 35, 3, 3);
			}
		} catch (Exception e) {
			g.fillRect(x * 28 + 25, y * 35, 3, 3);
		}
		try {
			if (!PoliceChase.grid[y][x - 1].canCollide){
				g.fillRect(x * 28, y * 35, 3, 35);
			}
		} catch (Exception e) {
			g.fillRect(x * 28, y * 35, 3, 35);
		}
		try {
			if (!PoliceChase.grid[y + 1][x + 1].canCollide){
				g.fillRect(x * 28 + 25, y * 35 + 32, 3, 3);
			}
		} catch (Exception e) {
			g.fillRect(x * 28 + 25, y * 35 + 32, 3, 3);
		}
		try {
			if (!PoliceChase.grid[y + 1][x].canCollide){
				g.fillRect(x * 28, y * 35 + 32, 28, 3);
			}
		} catch (Exception e) {
			g.fillRect(x * 28, y * 35 + 32, 28, 3);
		}
		try {
			if (!PoliceChase.grid[y + 1][x - 1].canCollide){
				g.fillRect(x * 28, y * 35 + 32, 3, 3);
			}
		} catch (Exception e) {
			g.fillRect(x * 28, y * 35 + 32, 3, 3);
		}
		try {
			if (!PoliceChase.grid[y][x + 1].canCollide){
				g.fillRect(x * 28 + 25, y * 35, 3, 35);
			}
		} catch (Exception e) {
			g.fillRect(x * 28 + 25, y * 35, 3, 35);
		}
		try {
			if (!PoliceChase.grid[y - 1][x - 1].canCollide){
				g.fillRect(x * 28, y * 35, 3, 3);
			}
		} catch (Exception e) {
			g.fillRect(x * 28, y * 35, 3, 3);
		}

	}

}
