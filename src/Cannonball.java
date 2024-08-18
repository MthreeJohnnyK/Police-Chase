import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cannonball extends Ammo{
	public static long fireTime = 3300000000L;
	public static int preferredRange = 550;
	public static double speed = 1.8;
	public Cannonball(Car car) {
		super(car, 1.8, 4, Assets.imgs.get(car.team ? "BlueCannonball" : "RedCannonball"));
		if (car instanceof UAV || car instanceof Minicar || car instanceof Sentry) {
			Hp = 1;
			rect.width = 5;
			rect.height = 5;
		}
	}
}
