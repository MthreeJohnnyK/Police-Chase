
public class MinicarSpawn extends PowerUp {

	public MinicarSpawn(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void activate(Car c) {
		Screen.carsToAdd.add(new Minicar(c));
	}
	@Override
	public boolean pickupCheck(Penguin p) {
		return true;
	}

}
