import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Shotgun extends Ammo{
	public static long fireTime = 2500000000L;
	public Shotgun(Car car) {
		super(car, 1.7, 1, Assets.imgs.get(car.team ? "BlueShotgun" : "RedShotgun"));
		rect.width = 8;
		rect.height = 8;
		Screen.carsToAdd.add(new Shotgun(car, Math.PI/16));
		Screen.carsToAdd.add(new Shotgun(car, Math.PI/32));
		Screen.carsToAdd.add(new Shotgun(car, -Math.PI/32));
		Screen.carsToAdd.add(new Shotgun(car, -Math.PI/16));
	}
	public Shotgun(Car car, double offset) {
		super(car, 1.7, 1, Assets.imgs.get(car.team ? "BlueShotgun" : "RedShotgun"));
		theta += offset;
		rect.width = 8;
		rect.height = 8;
	}
	public void fire() {
		/*Screen.carsToRemove.add(this);
		Shotgun sh = new Shotgun(this, Math.PI/10);
		sh.Hp = 1;
		rect.width = 6;
		rect.height = 6;
		Screen.carsToAdd.add(sh);
		sh = new Shotgun(this, -Math.PI/10);
		sh.Hp = 1;
		Screen.carsToAdd.add(sh);
		rect.width = 6;
		rect.height = 6;*/
	}
}
