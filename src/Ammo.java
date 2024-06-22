import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Ammo extends Car{
	Car from;
	public Ammo(Car car, double speed, int dmg, BufferedImage img) {
		super((int) (car.rect.getCenterX() - 5 + 10 * Math.cos(car.theta)), (int) (car.rect.getCenterY() - 5 - 10 * Math.sin(car.theta)), car.theta, speed, dmg, img, null, car.team);
		rect.width = 10;
		rect.height = 10;
		from = car;
	}
	@Override
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		for (Car c: Screen.cars) {
			if (c.team != this.team && c.rect.intersects(this.rect) && !(c instanceof Ammo)) {
				c.Hp -= Hp;
				Screen.carsToRemove.add(this);
			}
		}
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
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
		
	}
	@Override
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		rect.x += steps * Math.cos(theta);
		if (touchingWall()) {
			Screen.carsToRemove.add(this);
		}
		rect.y -= steps * Math.sin(theta);
		if (touchingWall()) {
			Screen.carsToRemove.add(this);
		}
		lastMove = System.nanoTime();
	}
}
