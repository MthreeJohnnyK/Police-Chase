import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Police2 extends Car implements KeyListener{
	private boolean up, left, down, right;
	public Police2(int x, int y, double theta, Class ammo, Ability a) {
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
			case KeyEvent.VK_Q:
				fire();
				break;
			case KeyEvent.VK_E:
				activate();
				break;
		}
		
	}
}
