import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Car {
	public int Hp; 
	private int mHp;
	private double theta;
	protected double speed;
	public Rect rect;
	private BufferedImage img;
	private Class ammo;
	private long lastMove = System.nanoTime();
	private long lastTurn = System.nanoTime();
	public Car(int x, int y, double theta, double speed, int Hp, BufferedImage img, Class ammo) {
		this.Hp = Hp;
		this.mHp = Hp;
		this.theta = theta;
		this.speed = speed;
		this.rect = new Rect(x, y, 20, 20);
		this.img = img;
		this.ammo = ammo;
	}
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		g.setColor(Color.red);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) rect.width, 3);
		g.setColor(Color.blue);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * Hp/mHp), 3);
		if (Hp <= 0) {
			Screen.carsToRemove.add(this);
		}
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	public void move(double steps) {
		if (System.nanoTime() < lastMove + 10000000) {
			return;
		}
		steps *= (System.nanoTime() - lastMove)/10000000.0;
		rect.x += steps * Math.cos(theta);
		if (touchingWall()) {
			rect.x -= steps * Math.cos(theta);
		}
		rect.y -= steps * Math.sin(theta);
		if (touchingWall()) {
			rect.y += steps * Math.sin(theta);
		}
		lastMove = System.nanoTime();
	}
	public void turn(double theta) {
		if (System.nanoTime() < lastTurn + 10000000) {
			return;
		}
		theta *= (System.nanoTime() - lastTurn)/10000000.0;
		this.theta += theta;
		this.theta += 2 * Math.PI;
		this.theta %= 2 * Math.PI;
		lastTurn = System.nanoTime();
	}
	public Point getGridLocation() {
		return new Point((int) (rect.getCenterX()/28), (int) (rect.getCenterY()/35));
	}
	public double getRadians() {
		return theta;
	}
	public boolean touchingWall() {
		Point loc = getGridLocation();
		return PoliceChase.grid[loc.y][loc.x]; 
	}
	public void fire() {
		
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
	public static Point getRandomLocation() {
		Point target = new Point();
		do {
			target.x = (int) (Math.random() * 48) + 1; 
			target.y = (int) (Math.random() * 23) + 1; 
		} while (!isAccessible(target.x, target.y));
		return target;
	}
	public static boolean isAccessible(int x, int y) {
		try {
			return !PoliceChase.grid[y][x];
		} catch (Exception e) {
			return false;
		}
	}
	public static Point getGridLocation(double x, double y) {
		return new Point((int) (x/28), (int) (y/35));
	}
	public static boolean touchingWall(Point p) {
		return PoliceChase.grid[p.y][p.x]; 
	}
}
