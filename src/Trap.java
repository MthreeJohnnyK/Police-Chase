import java.awt.Color;
import java.awt.Graphics;

public class Trap extends Car {
	protected int visibility = 16;
	protected int range;
	protected long activ = -1L;
	private long lastInvis = System.nanoTime();
	protected Car from;
	public Trap(int range, int Hp, Car c) {
		super((int) c.rect.x, (int) c.rect.y, 0, 0, Hp, null, null, null, c.team);
		this.range = range;
		this.from = c;
	}
	@Override
	public void paint(Graphics g) {
		if (System.nanoTime() >= lastInvis + 500000000L) {
			visibility -= visibility <= 0 ? 0 : 1;
			lastInvis = System.nanoTime();
		}
		if (Hp <= 0) {
			Screen.carsToRemove.add(this);
		}
		if (Hp < mHp) {
			visibility = 16;
			activ = activ == -1 ? System.nanoTime() : activ;
		}
		if (visibility > 0) {
			g.setColor(new Color(0, 0, 0, visibility * 16 - 1));
			g.drawOval((int) (rect.getCenterX() - range), (int) (rect.getCenterY() - range), range * 2, range * 2);
			g.setColor(new Color(255, 0, 0, visibility * 16 - 1));
			g.fillRect((int) rect.x + (int) (rect.width * Hp/mHp), (int) rect.y - 3, (int) (rect.width * (mHp - Hp)/mHp), 3);
			g.setColor(new Color(0, 0, 255, visibility * 16 - 1));
			g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * Hp/mHp), 3);
		}
		for (Car c: Screen.cars) {
			if (!(c instanceof Trap) && c.team != team && MathUtils.distanceTo(rect, c.rect) <= range) {
				visibility = 16;
				if (!(c instanceof Ammo)) {
					activ = activ == -1 ? System.nanoTime() : activ;
				}
			}
		}
		if (activ != -1L && System.nanoTime() >= activ + 1500000000L) {
			fire();
		}
	}
	@Override 
	public void fire() {
		Screen.carsToRemove.add(this);
	}

}
