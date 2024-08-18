import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Gas extends Ammo{
	public static long fireTime = 1500000000L;
	public static int limit = 180;
	public static int preferredRange = 180;
	public static double speed = 0.55;
	public static boolean penetrable = true;
	private static HashMap<Car, Long> dmgQueue = new HashMap<Car, Long>();
	private double moved = 0;
	private long spawnTime = System.nanoTime();
	public Gas(Car car) {
		super(car, 0.55, 1, Assets.imgs.get(car.team ? "BlueGas" : "RedGas"));
		rect.width = 6;
		rect.height = 6;
		Screen.carsToAdd.add(new Gas(car, Math.PI/15));
		Screen.carsToAdd.add(new Gas(car, -Math.PI/15));
	}
	public Gas(Car car, double offset) {
		super(car, 0.55, 1, Assets.imgs.get(car.team ? "BlueGas" : "RedGas"));
		theta += offset;
		rect.width = 6;
		rect.height = 6;
	}
	@Override
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		for (Car c: Screen.cars) {
			if (c.team != this.team && c.rect.intersects(this.rect) && !(c instanceof Ammo)) {
				dmgQueue.putIfAbsent(c, System.nanoTime());
				if (System.nanoTime() > dmgQueue.get(c) + 310000000) {
					if (c.rect.intersects(this.rect)  && System.nanoTime() < dmgQueue.get(c) + 315000000) {
						c.Hp -= Hp;
					}
					dmgQueue.remove(c);
				}
			}
		}
		move(super.speed);
		//speed = moved/500 + 0.23;
		rect.width = moved/3 + 5;
		rect.height = moved/3 + 5;
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		moved += steps;
		if (moved > limit) {
			Screen.carsToRemove.add(this);
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		rect.x += steps * Math.cos(theta);
		if (rect.getCenterX() < 28 || rect.getCenterX() > 1372) {
			Screen.carsToRemove.add(this);
		}
		rect.y -= steps * Math.sin(theta);
		if (rect.getCenterY() < 35 || rect.getCenterY() > 840) {
			Screen.carsToRemove.add(this);
		}
		lastMove = System.nanoTime();
	}
	
}
