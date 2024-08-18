import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Ability {
	protected long lastActivate = System.nanoTime();
	protected long cooldown;
	protected long duration;
	private BufferedImage img;
	public Ability(long cooldown, long duration, BufferedImage img) {
		this.cooldown = cooldown;
		this.duration = duration;
		this.img = img;
		lastActivate -= cooldown/2;
	}
	public void paint(Car c, Graphics g) {
		if (System.nanoTime() > lastActivate + duration) {
			return;
		}
		drawImage(c.rect.getCenterX(), c.rect.getCenterY(), c.rect.width, c.rect.height, c.theta, img, g);
	}
	public boolean activate(Car c) {
		if (System.nanoTime() < lastActivate + cooldown && c.Hp > 0) {
			return false;
		}
		lastActivate = System.nanoTime();
		return true;
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
}
