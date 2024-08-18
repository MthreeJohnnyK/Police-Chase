
public class AbilityCharge extends PowerUp {

	public AbilityCharge(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void activate(Car c) {
		c.a.lastActivate -= 10000000000L;
	}
	@Override
	public boolean pickupCheck(Penguin p) {
		if (p.a == null) {
			return false;
		}
		return p.a.cooldown - (System.nanoTime() - p.a.lastActivate) > 5000000000L;
	}

}
