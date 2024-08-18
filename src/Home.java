import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Home extends Ability {
	double x = -100;
	double y = -100;
	public Home() {
		super(35000000000L, 500000000, null);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean activate(Car c) {
		boolean b = super.activate(c);
		if (b) {
			x = c.rect.getCenterX();
			y = c.rect.getCenterY();
			c.rect.x = c.spawnX;
			c.rect.y = c.spawnY;
		}
		return b;
	}
	@Override
	public void paint(Car c, Graphics g) {
		g.setColor(c.team ? Color.blue : Color.red);
    	g.drawOval(c.spawnX - 3, c.spawnY - 3, 26, 26);
    	g.drawOval(c.spawnX + 1, c.spawnY + 1, 18, 18);
    	g.drawOval(c.spawnX + 5, c.spawnY + 5, 10, 10);
    	g.setColor(Color.black);
		g.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 8));
		g.drawString(c.getClass().getSimpleName(), c.spawnX - 2, c.spawnY + 29);
		if (System.nanoTime() > lastActivate + duration) {
			x = -100;
			y = -100;
			return;
		}
		double scale = 1 - (System.nanoTime() - lastActivate)/(double) duration;
		g.setColor(c.team ? Color.blue : Color.red);
		g.fillOval((int) (x - 8 * scale), (int) (y - 8 * scale), (int) (16 * scale), (int) (16 * scale));
	}

}
