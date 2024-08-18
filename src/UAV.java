import java.awt.Color;
import java.awt.Graphics;

public class UAV extends Car {
	private double dir = 0.0;
	private long lastLoad = System.nanoTime();
	private int ammos;
	public UAV(int x, int y, double theta, boolean team) {
		super(x, y, theta, 0.5, 5, Assets.newImage(team ? "BlueDrone.png" : "RedDrone.png"), Cannonball.class, null, team);
		dir = theta;
		ammos = Hp;
	}
	@Override
	public void paint(Graphics g) {
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, dir, img, g);
		g.setColor(Color.black);
		g.drawOval((int) (rect.getCenterX() - 150), (int) (rect.getCenterY() - 150), 300, 300);
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
		if (System.nanoTime() >= lastLoad + 1000000000) {
			ammos ++;
			lastLoad = System.nanoTime();
		}
		if (ammos > Hp) {
			ammos = Hp;
		}
		Car closest = null;
		double distance = Double.MAX_VALUE;
		for (Car c: Screen.cars) {
			double d = MathUtils.distanceTo(rect, c.rect);
			if (!(c instanceof Ammo) && c.team != team && d < 150 && d < distance) {
				double angle = MathUtils.getAngle(rect, c.rect.getCenterX(),  c.rect.getCenterY());
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
		move(speed);
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	public void fire() {
		for (Car c: Screen.cars) {
			if (c instanceof Ammo && ((Ammo) c).from == this) {
				c.fire();
			}
		}
		try {
			if (System.nanoTime() < lastFire + 230000000L || ammos <= 0) {
				return;
			}
			Screen.carsToAdd.add((Car) ammo.getConstructor(Car.class).newInstance(this));
			ammos --;
			lastFire = System.nanoTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		rect.x += steps * Math.cos(dir);
		if (rect.x < 28 || rect.x > 1372) {
			Screen.carsToRemove.add(this);
		}
		rect.y -= steps * Math.sin(dir);
		if (rect.y < 35 || rect.y > 840) {
			Screen.carsToRemove.add(this);
		}
		lastMove = System.nanoTime();
	}
}
