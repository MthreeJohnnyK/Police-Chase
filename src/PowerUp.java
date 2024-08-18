import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public class PowerUp extends MapObject {
	private BufferedImage img;
	private static final Class[] refs = {HealOrb.class, AbilityCharge.class, MinicarSpawn.class};
	public PowerUp(int x, int y) {
		super(x, y, false);
		this.img = Assets.newImage(this.getClass().getSimpleName() + ".png");
	}
	@Override
	public void paint(Graphics g) {
		drawImage(x * 28 + 14, y * 35 + 17.5, 28, 28, 0, img, g);
		Car[] list = {PoliceChase.Police1, PoliceChase.Police2, PoliceChase.Thief};
		for (Car c: list) {
			if (c != null && c.getGridLocation().x == x && c.getGridLocation().y == y) {
				activate(c);
				PoliceChase.grid[y][x] = new Air(x, y);
				Screen.powerups.remove(this);
			}
		}
	}
	public static void drawImage(double x, double y, double width, double height, double direction, BufferedImage image, Graphics g) {
	  	  AffineTransform at = new AffineTransform();
	  	  at.translate(x, y);
	  	  at.rotate(-direction);
	  	  at.scale(width/image.getWidth(), height/image.getHeight());
	  	  at.translate(-image.getWidth(null) / 2, -image.getHeight(null) / 2);
	  	  Graphics2D g2d = (Graphics2D) g;
	  	  g2d.drawImage(image, at, null);
	}
	public void activate(Car c) {
		
	}
	public boolean pickupCheck(Penguin p) {
		return true;
	}
	public static void newPowerUp() {
		int x;
		int y;
		do {
			x = (int) (Math.random() * 48) + 1;
			y = (int) (Math.random() * 23) + 1;
		} while (!(PoliceChase.grid[y][x] instanceof Air));
		try {
			PowerUp p = (PowerUp) refs[(int) (Math.random() * refs.length)].getDeclaredConstructor(int.class, int.class).newInstance(x, y);
			Screen.powerups.add(p);
			PoliceChase.grid[y][x] = p;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
