import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Thief extends Car implements KeyListener{
	private boolean up, left, down, right;
	public Thief(int x, int y, double theta, Class ammo) {
		super(x, y, theta, 0.5, 25, Assets.newImage("Red.png"), ammo);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (up) {
			move(speed);
		} 
		if (left) {
			turn(speed * 0.06);
		}
		if (down) {
			move(-speed);
		}
		if (right) {
			turn(-speed * 0.06);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case KeyEvent.VK_W:
				up = true;
				break;
			case KeyEvent.VK_A:
				left = true;
				break;
			case KeyEvent.VK_S:
				down = true;
				break;
			case KeyEvent.VK_D:
				right = true;
				break;			
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case KeyEvent.VK_W:
				up = false;
				break;
			case KeyEvent.VK_A:
				left = false;
				break;
			case KeyEvent.VK_S:
				down = false;
				break;
			case KeyEvent.VK_D:
				right = false;
				break;			
		}
		
	}
}
