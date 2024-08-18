import java.awt.Color;
import java.awt.Graphics;

public class Towrope extends Trap { 
	private boolean select;
	private Car target = null;
	private long lastTow = System.nanoTime();
	public Towrope(Car c) {
		super(50, 8, c);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (visibility > 0) {
			g.setColor(new Color(150, 150, 150, visibility * 16 - 1));
			g.fillOval((int) (rect.getCenterX() - 3), (int) (rect.getCenterY() - 3), 6, 6);
		}
		if (target != null) {
			MathUtils.rayCast(target.rect, MathUtils.getAngle(target.rect, rect.getCenterX(), rect.getCenterY()), this, g);
		}
		if (System.nanoTime() > lastTow + 10000000) {
			lastTow = System.nanoTime() - 10000000;
		}
		if (activ != -1L) {
			fire();
		}
	}
	@Override 
	public void fire() {
		if (System.nanoTime() < lastTow + 10000000) {
			return;
		}
		if (!select) {
			select = true;
			double d = Double.MAX_VALUE;
			for (Car c: Screen.cars) {
				double dist = MathUtils.distanceTo(rect, c.rect);
				if (!(c instanceof Ammo) && c.team != team && dist <= range && dist < d) {
					d = dist;
					target = c;
				}
			}
		}
		if (target != null && !rect.intersects(target.rect)) {
			target.forceMove(rect.getCenterX(), rect.getCenterY(), 0.5 * (System.nanoTime() - lastTow)/10000000.0);
		}
		lastTow = System.nanoTime();
		if (System.nanoTime() >= activ + 10000000000L) {
			super.fire();
		}
	}

}
