import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

public class Traps extends Ability {
	private Class trap;
	public Traps(Class trap) {
		super(35000000000L, 0, null);
		// TODO Auto-generated constructor stub
		lastActivate -= cooldown/2;
		this.trap = trap;
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			try {
				Screen.carsToAdd.add((Car) trap.getDeclaredConstructor(Car.class).newInstance(c));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	@Override
	public void paint(Car c, Graphics g) {
		
	}

}
