import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bomb extends Ammo{
	public static long fireTime = 3000000000L;
	private long explosionTime = -1L;
	public static int preferredRange = 550;
	private boolean access = false;
	public Bomb(Car car) {
		super(car, 2.3, 2, Assets.imgs.get(car.team ? "BlueBomb" : "RedBomb"));
	}
	@Override
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		if (explosionTime != -1L) {
			g.setColor(team ? Color.cyan : Color.pink);
			g.fillOval((int) rect.getCenterX() - 50, (int) rect.getCenterY() - 50, 100, 100);
			if (System.nanoTime() > explosionTime + 500000000L) {
				Screen.carsToRemove.add(this);
			}
			return;
		}
		for (Car c: Screen.cars) {
			if (c.team != this.team && c.rect.intersects(this.rect) && !(c instanceof Ammo)) {
				access = true;
				fire();
			}
		}
		move(speed);
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	@Override
	public void fire() {
		if (from instanceof Penguin && !access) {
			return;
		}
		if (explosionTime != -1L) {
			return;
		}
		for (Car c: Screen.cars) {
			if (c.team != this.team && MathUtils.distanceTo(rect, c.rect) <= 50 && !(c instanceof Ammo)) {
				c.Hp -= Hp;
			}
		}
		explosionTime = System.nanoTime();
	}
	@Override
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		rect.x += steps * Math.cos(theta);
		if (touchingWall()) {
			access = true;
			fire();
			return;
		}
		rect.y -= steps * Math.sin(theta);
		if (touchingWall()) {
			access = true;
			fire();
			return;
		}
		lastMove = System.nanoTime();
	}
	
}
