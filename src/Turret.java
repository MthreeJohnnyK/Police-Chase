import java.awt.Graphics;

public class Turret extends Ability {

	public Turret() {
		super(55000000000L, 0, null);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			Screen.carsToAdd.add(new Sentry((int) c.rect.x, (int) c.rect.y, c.theta, c.team));
		}
		return b;
	}
	@Override
	public void paint(Car c, Graphics g) {
		
	}

}
