import java.awt.Color;
import java.awt.Graphics;

public class Trap extends Car {
	protected int visibility = 16;
	protected int range;
	protected long activ = -1L;
	private long lastInvis = System.nanoTime();
	public Trap(int x, int y, int range, int Hp, boolean team) {
		super(x, y, 0, 0, Hp, null, null, null, team);
		this.range = range;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		if (System.nanoTime() >= lastInvis + 500000000L) {
			visibility -= visibility <= 0 ? 0 : 1;
			lastInvis = System.nanoTime();
		}
		if (Hp < mHp) {
			visibility = 16;
			activ = activ == -1 ? System.nanoTime() : activ;
		}
		if (visibility > 0) {
			g.setColor(new Color(0, 0, 0, visibility * 16 - 1));
			g.drawOval((int) (rect.getCenterX() - range), (int) (rect.getCenterY() - range), range * 2, range * 2);
			g.setColor(new Color(255, 0, 0, visibility * 16 - 1));
			g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * (mHp - Hp)/mHp), 3);
			g.setColor(new Color(0, 0, 255, visibility * 16 - 1));
			g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * Hp/mHp), 3);
		}
		for (Car c: Screen.cars) {
			if (!(c instanceof Trap) && !(c instanceof Ammo) && c.team != team && MathUtils.distanceTo(rect, c.rect) <= range) {
				visibility = 16;
				activ = activ == -1 ? System.nanoTime() : activ;
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
