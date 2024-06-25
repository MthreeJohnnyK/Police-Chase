import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Lazer extends Ammo{
	public static long fireTime = 3000000000L;
	public static int preferredRange = 680;
	private boolean bounced = false;
	public Lazer(Car car) {
		super(car, 1.8, 2, Assets.imgs.get(car.team ? "BlueLazer" : "RedLazer"));
	}
	@Override
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		for (Car c: Screen.cars) {
			if (c.team != this.team && c.rect.intersects(this.rect) && !(c instanceof Ammo)) {
				c.Hp -= Hp;
				Screen.carsToRemove.add(this);
				break;
			}
		}
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width * 2.5, rect.height * 0.5, theta, img, g);
		move(speed);
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	@Override
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		rect.x += steps * Math.cos(theta);
		if (touchingWall()) {
			if (!bounced) {
				bounced = true;
				while(touchingWall()) {
					rect.x -=  Math.cos(theta);
				}
				theta = Math.PI - theta;
				return;
			} else {
				Screen.carsToRemove.add(this);
			}
		}
		rect.y -= steps * Math.sin(theta);
		if (touchingWall()) {
			if (!bounced) {
				bounced = true;
				while(touchingWall()) {
					rect.y += Math.sin(theta);
				}
				theta = 2 * Math.PI - theta;
				return;
			} else {
				Screen.carsToRemove.add(this);
			}
		}
		lastMove = System.nanoTime();
	}
}
