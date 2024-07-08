import java.awt.Color;
import java.awt.Graphics;

public class Sentry extends Car {

	public Sentry(int x, int y, double theta, boolean team) {
		super(x, y, theta, 0.0, 8, Assets.newImage(team ? "BlueSentry.png" : "RedSentry.png"), Cannonball.class, null, team);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		g.setColor(Color.black);
		g.drawOval((int) (rect.getCenterX() - 165), (int) (rect.getCenterY() - 165), 330, 330);
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
			if (!(c instanceof Ammo) && c.team != team && d < 165 && d < distance) {
				double angle = MathUtils.getAngle(rect, c.rect.getCenterX(),  c.rect.getCenterY());
				if (MathUtils.rayCast(rect, angle, c, null)) {
					closest = c;
					distance = d;
				}
			}
		}
		if (closest != null) {
			theta = MathUtils.getAngle(rect, closest.rect.getCenterX(),  closest.rect.getCenterY());
			fire();
		}
	}
	public void fire() {
		for (Car c: Screen.cars) {
			if (c instanceof Ammo && ((Ammo) c).from == this) {
				c.fire();
			}
		}
		try {
			if (System.nanoTime() < lastFire + 1000000000L) {
				return;
			}
			Screen.carsToAdd.add((Car) ammo.getConstructor(Car.class).newInstance(this));
			lastFire = System.nanoTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
