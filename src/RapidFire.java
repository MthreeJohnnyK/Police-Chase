import java.awt.Graphics;

public class RapidFire extends Ability {
	private boolean activated = false;
	public RapidFire() {
		super(50000000000L, 1000000000, null);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			try {
				Screen.carsToAdd.add((Car) c.ammo.getConstructor(Car.class).newInstance(c));
				activated = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	public void paint(Car c, Graphics g) {
		if (System.nanoTime() > lastActivate + duration) {
			return;
		}
		if (System.nanoTime() > lastActivate + duration/2 && activated) {
			try {
			Screen.carsToAdd.add((Car) c.ammo.getConstructor(Car.class).newInstance(c));
			activated = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
