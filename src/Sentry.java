import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

public class Sentry extends Car {
	private int ammos;
	private long lastLoad = System.nanoTime();
	public Sentry(int x, int y, double theta, boolean team) {
		super(x, y, theta, 0.0, 5, Assets.newImage(team ? "BlueSentry.png" : "RedSentry.png"), Cannonball.class, null, team);
		ammos = Hp/2;
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
		g.setColor(Color.green);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * ammos/mHp), 3);
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
				double angle = MathUtils.getAngle(rect, c.rect.getCenterX(), c.rect.getCenterY());
				if (MathUtils.rayCast(rect, angle, c, 1, null)) {
					closest = c;
					distance = d;
				}
			}
		}
		if (closest != null) {
			theta = MathUtils.getAngle(rect, closest.rect.getCenterX(),  closest.rect.getCenterY());
			fire();
			if (ammos > 0) {
				lastLoad = System.nanoTime();
			}
		}
		if (System.nanoTime() >= lastLoad + 1000000000) {
			ammos ++;
			lastLoad = System.nanoTime();
		}
		if (ammos > Hp) {
			ammos = Hp;
		}
	}
	public void fire() {
		if (System.nanoTime() < lastFire + 230000000L || ammos <= 0) {
			return;
		}
		try {
			Screen.carsToAdd.add((Car) ammo.getConstructor(Car.class).newInstance(this));
			ammos--;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastFire = System.nanoTime();
	}

}
