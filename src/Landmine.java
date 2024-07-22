import java.awt.Graphics;

public class Landmine extends Ability {
	public Landmine() {
		super(35000000000L, 0, null);
		// TODO Auto-generated constructor stub
		lastActivate -= cooldown/2;
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			Screen.carsToAdd.add(new Mine((int) c.rect.x, (int) c.rect.y, c.team));
		}
		return b;
	}
	@Override
	public void paint(Car c, Graphics g) {
		
	}

}
