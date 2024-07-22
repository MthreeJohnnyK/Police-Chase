import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Mortar extends Ammo{
	public static long fireTime = 5000000000L;
	private long explosionTime;
	public static int preferredRange = 880;
	private boolean access = false;
	private boolean exploded = false;
	public static boolean penetrable = true;
	public Mortar(Car car) {
		super(car, 0, 5, Assets.imgs.get(car.team ? "BlueMortar" : "RedMortar"));
		Car closest = null;
		double dist = Double.MAX_VALUE;
		for (Car c: Screen.cars) {
			if (!(c instanceof Ammo) && c.team != team) {
				double d = MathUtils.angularDistance(theta, MathUtils.getAngle(rect, c.rect.getCenterX(), c.rect.getCenterY()));
				if (d < dist) {
					dist = d;
					closest = c;
				}
			}
		}
		dist = MathUtils.distanceTo(rect, closest.rect);
		move(dist > 880 ? 880 : dist);
		rect.x -= 55;
		rect.y -= 55;
		rect.width = 110;
		rect.height = 110;
		explosionTime = System.nanoTime() + (long) ((2200/(dist + 300)) * 1000000000L);
	}
	@Override
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		g.setColor(team ? Color.blue : Color.red);
		double diff = (explosionTime - System.nanoTime())/100000000.0;
		g.drawOval((int) (rect.getCenterX() - diff - 55), (int) (rect.getCenterY() - diff - 55), (int) (diff * 2 + 110), (int) (diff * 2 + 110));
		if (System.nanoTime() > explosionTime) {
			g.setColor(team ? Color.cyan : Color.pink);
			access = true;
			fire();
			g.fillOval((int) rect.getCenterX() - 55, (int) rect.getCenterY() - 55, 110, 110);
			if (System.nanoTime() > explosionTime + 500000000L) {
				Screen.carsToRemove.add(this);
			}
			return;
		}
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	@Override
	public void fire() {
		if (!access) {
			return;
		}
		access = false;
		if (exploded) {
			return;
		}
		exploded = true;
		if (System.nanoTime() < explosionTime) {
			return;
		}
		for (Car c: Screen.cars) {
			if (c.team != team && MathUtils.distanceTo(rect, c.rect) <= 55 && !(c instanceof Ammo)) {
				c.Hp -= Hp;
			}
		}
	}
	@Override
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		rect.x += steps * Math.cos(theta);
		rect.y -= steps * Math.sin(theta);
		lastMove = System.nanoTime();
	}
	
}
