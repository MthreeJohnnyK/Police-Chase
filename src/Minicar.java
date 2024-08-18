import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Minicar extends Penguin{
	public static long fireTime = 5000000000L;
	public static int preferredRange = 550;
	public static double speed = 0.5;
	public static boolean penetrable = true;
	private Point target = getGridLocation();
	private Car car;
	int[][] grid = new int[25][50];
	public Minicar(Car car) {
		super((int) car.rect.x + 5, (int) car.rect.y + 5, car.theta, Cannonball.class, null, car.team);
		rect.width = 13.5;
		rect.height = 13.5;
		this.Hp = 1;
		this.mHp = 1;
		this.respawns = 0;
		this.car = car;
		int count = 0;
		for (int i = Screen.cars.size() - 1; i >= 0; i --) {
			Car c = Screen.cars.get(i);
			if (c instanceof Minicar && ((Minicar) c).car == car) {
				count ++;
				if (count >= 3) {
					c.Hp = 0;
				}
			}
		}
	}
	public void respawn() {
		super.respawn();
		super.curTarget = getGridLocation();
	}
}