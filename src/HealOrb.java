
public class HealOrb extends PowerUp {

	public HealOrb(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void activate(Car c) {
		c.Hp ++;
	}
	@Override
	public boolean pickupCheck(Penguin p) {
		return p.Hp < p.mHp;
	}
}
