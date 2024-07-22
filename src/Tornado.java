import java.awt.Color;
import java.awt.Graphics;

public class Tornado extends Ammo {
	private double dir = 0.0;
	private double steps = 0;
	private long lastBlew = System.nanoTime();
	public Tornado(Car car, double theta) {
		super(car, 0.8, 0, null);
		dir = theta;
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(team ? Color.blue : Color.red);
		double range = steps/10 + 50;
		for (double d = theta; range > 30; d += Math.PI/6) {
			d %= 2 * Math.PI;
			g.fillOval((int) (range * Math.cos(d) - range/16 + rect.getCenterX()) , (int) (range * Math.sin(d) - range/16 + rect.getCenterY()), (int) (range/8), (int) (range/8));
			range -= 3;
		}
		g.drawOval((int) (rect.getCenterX() - steps/10 - 50), (int) (rect.getCenterY() - steps/10 - 50), (int) (steps/5 + 100), (int) (steps/5 + 100));
		if (System.nanoTime() >= lastBlew + 10000000) {
			for (Car c: Screen.cars) {
				if (c.team != team && MathUtils.distanceTo(rect, c.rect) <= steps/10 + 50) {
					c.forceMove(rect.getCenterX(), rect.getCenterY(), 1.5 * (System.nanoTime() - lastMove)/10000000.0);
				}
			}
			theta -= Math.PI/135;
			lastBlew = System.nanoTime();
		}		
		move(speed);
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
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
		this.steps += steps;
	}
}
