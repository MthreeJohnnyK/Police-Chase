import java.awt.Color;
import java.awt.Graphics;

public class Landmine extends Trap { 
	public Landmine(Car c) {
		super(50, 5, c);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (visibility > 0) {
			g.setColor(new Color(150, 150, 150, visibility * 16 - 1));
			g.fillOval((int) (rect.getCenterX() - 13), (int) (rect.getCenterY() - 13), 26, 26);
			g.setColor(new Color(team ? 0 : 255, 0, team ? 255 : 0, visibility * 16 - 1));
			g.fillOval((int) (rect.getCenterX() - 5), (int) (rect.getCenterY() - 5), 10, 10);
			if (System.nanoTime() > activ + 1400000000L && activ != -1L) {
				g.setColor(team ? Color.cyan : Color.pink);
				g.fillOval((int) (rect.getCenterX() - range), (int) (rect.getCenterY() - range), range * 2, range * 2);
			}	
		}
	}
	@Override 
	public void fire() {
		super.fire();
		for (Car c: Screen.cars) {
			if (c.team != team && MathUtils.distanceTo(rect, c.rect) <= range) {
				c.Hp -= 5;
			}
		}
	}

}
