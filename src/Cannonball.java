import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cannonball extends Ammo{
	public static long fireTime = 3000000000L;
	public Cannonball(Car car) {
		super(car, 1.5, 4, Assets.imgs.get(car.team ? "BlueCannonball" : "RedCannonball"));
	}
}
