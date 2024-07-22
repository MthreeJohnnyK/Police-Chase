import java.awt.Color;
import java.awt.Graphics;

public class Sentry extends Car {
	private int range;
	public Sentry(int x, int y, Class ammo, double theta, boolean team) {
		super(x, y, theta, 0.0, 8, Assets.newImage(team ? "BlueSentry.png" : "RedSentry.png"), ammo, null, team);
		try {
			range = ammo.getField("preferredRange").getInt(null);
		} catch (Exception e) {
			e.printStackTrace();
			range = 165;
		}
	}
	@Override
	public void paint(Graphics g) {
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		g.setColor(Color.black);
		g.drawOval((int) (rect.getCenterX() - range), (int) (rect.getCenterY() - range), range * 2, range * 2);
		g.setColor(Color.red);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) rect.width, 3);
		g.setColor(Color.blue);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * Hp/mHp), 3);
		if (Hp <= 0) {
			Hp = 0;
			Screen.carsToRemove.add(this);
		} else if (Hp > mHp) {
		Hp = mHp;
		}
		Car closest = null;
		double distance = Double.MAX_VALUE;
		for (Car c: Screen.cars) {
			double d = MathUtils.distanceTo(rect, c.rect);
			if (!(c instanceof Ammo) && c.team != team && d < range && d < distance) {
				double angle = MathUtils.getAngle(rect, c.rect.getCenterX(),  c.rect.getCenterY());
				boolean ignore;
				try {
					ignore = ammo.getField("penetrable").getBoolean(null);
				} catch (Exception e) {
					ignore = false;
				}
				if (MathUtils.rayCast(rect, angle, c, ignore, range, null)) {
					closest = c;
					distance = d;
				}
			}
		}
		try {
			if (closest != null && System.nanoTime() >= lastFire + ammo.getField("fireTime").getLong(null) * 2) {
				theta = MathUtils.getAngle(rect, closest.rect.getCenterX(),  closest.rect.getCenterY());
				fire();
				if (ammo == Missile.class) {
					fire();
				}
			}
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void fire() {
		for (Car c: Screen.cars) {
			if (c instanceof Ammo && ((Ammo) c).from == this) {
				c.fire();
			}
		}
		for (Car c: Screen.carsToAdd) {
			if (c instanceof Ammo && ((Ammo) c).from == this) {
				c.fire();
			}
		}
		try {
			if (System.nanoTime() < lastFire + ammo.getField("fireTime").getLong(null) * 2) {
				return;
			}
			Screen.carsToAdd.add((Car) ammo.getConstructor(Car.class).newInstance(this));
			lastFire = System.nanoTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
