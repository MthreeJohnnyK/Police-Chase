import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Shotgun extends Ammo{
	public static long fireTime = 3300000000L;
	public static int limit = 180;
	public static int preferredRange = 30;
	private double moved = 0;
	public Shotgun(Car car) {
		super(car, 1.7, 1, Assets.imgs.get(car.team ? "BlueShotgun" : "RedShotgun"));
		rect.width = 6;
		rect.height = 6;
		Screen.carsToAdd.add(new Shotgun(car, Math.PI/11.5));
		Screen.carsToAdd.add(new Shotgun(car, Math.PI/23));
		Screen.carsToAdd.add(new Shotgun(car, -Math.PI/23));
		Screen.carsToAdd.add(new Shotgun(car, -Math.PI/11.5));
	}
	public Shotgun(Car car, double offset) {
		super(car, 1.7, 1, Assets.imgs.get(car.team ? "BlueShotgun" : "RedShotgun"));
		theta += offset;
		rect.width = 6;
		rect.height = 6;
	}
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		moved += steps;
		rect.x += steps * Math.cos(theta);
		if (touchingWall()) {
			Screen.carsToRemove.add(this);
		}
		rect.y -= steps * Math.sin(theta);
		if (touchingWall()) {
			Screen.carsToRemove.add(this);
		}
		if (moved > limit) {
			Screen.carsToRemove.add(this);
		}
		lastMove = System.nanoTime();
	}
}
