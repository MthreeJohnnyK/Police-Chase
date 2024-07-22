import java.awt.Graphics;

public class Wind extends Ability {

	public Wind() {
		super(35000000000L, 0, null);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			Screen.carsToAdd.add(new Tornado(c, c.theta));
		}
		return b;
	}
	@Override
	public void paint(Car c, Graphics g) {
		
	}

}
