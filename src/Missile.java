import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Missile extends Ammo{
	public static long fireTime = 3500000000L;
	public static int preferredRange = Integer.MAX_VALUE;
	public static double speed = 3.8;
	public Missile(Car car) {
		super(car, 0.6, 1, Assets.imgs.get(car.team ? "BlueMissile" : "RedMissile"));
	}
	@Override
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		for (Car c: Screen.cars) {
			if (c.team != this.team && c.Hp > 0 && c.rect.intersects(this.rect) && !(c instanceof Ammo)) {
				c.Hp -= super.speed > 3.8 ? Hp + 1 : Hp;
				Screen.carsToRemove.add(this);
				break;
			}
		}
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width * 2.5, rect.height, theta, img, g);
		move(super.speed);
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	@Override
	public void fire() {
		if (super.speed > 1) {
			return;
		}
		Car closest = null;
		double distance = Double.MAX_VALUE;
		for (Car c: Screen.cars) {
			if (!(c instanceof Ammo) && c.team != team && c.Hp > 0 && MathUtils.distanceTo(rect, c.rect) < distance) {
				double angle = MathUtils.getAngle(rect, c.rect.getCenterX(),  c.rect.getCenterY());
				if (MathUtils.rayCast(rect, angle, c, null)) {
					closest = c;
					distance = MathUtils.distanceTo(rect, c.rect);
				}
			}
		}
		if (closest != null && MathUtils.angularDistance(theta, MathUtils.getAngle(rect, closest.rect.getCenterX(),  closest.rect.getCenterY())) < Math.PI/1.4) {
			theta = MathUtils.getAngle(rect, closest.rect.getCenterX(),  closest.rect.getCenterY());
			super.speed = 1;
		}
	}
	@Override
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		super.speed += super.speed >= 1 ? steps/50 : 0;
		super.speed = super.speed > 8 ? 8 : super.speed;
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
