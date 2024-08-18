import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Minigun extends Ammo{
	public static long fireTime = 5800000000L;
	public static int preferredRange = 300;
	public static double speed = 1.4;
	private long spawnTime;
	public Minigun(Car car) {
		super(car, 1.4, 2, Assets.imgs.get(car.team ? "BlueMinigun" : "RedMinigun"));
		rect.width = 8;
		rect.height = 8;
		spawnTime = System.nanoTime();
		Screen.carsToAdd.add(new Minigun(car, 250000000L));
		Screen.carsToAdd.add(new Minigun(car, 500000000L));
	}
	public Minigun(Car car, long delay) {
		super(car, 1.4, 2, Assets.imgs.get(car.team ? "BlueMinigun" : "RedMinigun"));
		rect.width = 8;
		rect.height = 8;
		theta += (Math.random() - 0.5)/8;
		spawnTime = System.nanoTime() + delay;
	}
	@Override
	public void move(double steps) {
		if (System.nanoTime() >= spawnTime) {
			super.move(steps);
		}
	}
	
	
}
