import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Minigun extends Ammo{
	public static long fireTime = 2500000000L;
	private double steps = 0.0;
	public Minigun(Car car) {
		super(car, 1.4, 2, Assets.imgs.get(car.team ? "BlueMinigun" : "RedMinigun"));
		rect.width = 8;
		rect.height = 8;
	}
	public Minigun(Car car, double offset) {
		super(car, 1.4, 2, Assets.imgs.get(car.team ? "BlueMinigun" : "RedMinigun"));
		move(offset);
		rect.width = 8;
		rect.height = 8;
		theta += (Math.random() - 0.5)/100;
		steps = 166;
	}
	@Override
	public void move(double steps) {
		super.move(steps);
		this.steps += steps;
		if (this.steps > 80.0 && this.steps < 83.0) {
			this.steps = 83;
			Screen.carsToAdd.add(new Minigun(from, 0));
		}
		if (this.steps > 163.0 && this.steps < 166.0) {
			this.steps = 166;
			Screen.carsToAdd.add(new Minigun(from, 0));
		}
	}
	
}
