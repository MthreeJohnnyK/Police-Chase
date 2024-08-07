import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Police1 extends Car implements KeyListener{
	private boolean up, left, down, right;
	public Police1(int x, int y, double theta, Class ammo, Ability a) {
		super(x, y, theta, 0.5, 10, Assets.newImage("Blue.png"), ammo, a, true);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (up) {
			move(speed);
		} 
		if (left) {
			turn(speed * 0.05);
		}
		if (down) {
			move(-speed * 0.8);
		}
		if (right) {
			turn(-speed * 0.05);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case KeyEvent.VK_UP:
				up = true;
				break;
			case KeyEvent.VK_LEFT:
				left = true;
				break;
			case KeyEvent.VK_DOWN:
				down = true;
				break;
			case KeyEvent.VK_RIGHT:
				right = true;
				break;
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case KeyEvent.VK_UP:
				up = false;
				break;
			case KeyEvent.VK_LEFT:
				left = false;
				break;
			case KeyEvent.VK_DOWN:
				down = false;
				break;
			case KeyEvent.VK_RIGHT:
				right = false;
				break;	
			case KeyEvent.VK_SPACE:
				fire();
				break;
			case KeyEvent.VK_ENTER:
				activate();
				break;
		}
		
	}
}
