import java.awt.Graphics;

public class Turret extends Ability {
	private Class ammo;
	public Turret(Class ammo) {
		super(80000000000L, 0, null);
		// TODO Auto-generated constructor stub
		this.ammo = ammo;
		lastActivate -= cooldown/2;
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			Screen.carsToAdd.add(new Sentry((int) c.rect.x, (int) c.rect.y, ammo,  c.theta, c.team));
		}
		return b;
	}
	@Override
	public void paint(Car c, Graphics g) {
		
	}

}
