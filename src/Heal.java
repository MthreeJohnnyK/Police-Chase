

public class Heal extends Ability {
	public Heal() {
		super(35000000000L, 500000000, Assets.newImage("Heal.png"));
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			c.Hp += 3;
		}
		return b;
	}

}
