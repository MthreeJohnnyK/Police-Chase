import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Car {
	public int Hp; 
	protected int mHp;
	protected int respawns = 2;
	protected double theta;
	protected double speed;
	public Rect rect;
	public boolean team;
	protected boolean devMode = false;
	protected BufferedImage img;
	protected Class ammo;
	protected long lastMove = System.nanoTime() - 10000000;
	protected long lastTurn = System.nanoTime() - 10000000;
	protected long lastFire = System.nanoTime();
	protected Ability a;
	protected int spawnX;
	protected int spawnY;
	protected boolean moving = false;
	private double moveDir = 0.0;
	public Car(int x, int y, double theta, double speed, int Hp, BufferedImage img, Class ammo, Ability a, boolean team) {
		this.Hp = Hp;
		this.mHp = Hp;
		this.theta = theta;
		this.speed = speed;
		this.rect = new Rect(x, y, 20, 20);
		this.img = img;
		this.ammo = ammo;
		this.team = team;
		this.a = a;
		spawnX = x;
		spawnY = y;
	}
	public void paint(Graphics g) {
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		drawImage(rect.getCenterX(), rect.getCenterY(), rect.width, rect.height, theta, img, g);
		if (a != null) {
			a.paint(this, g);
		}
		if (devMode) {
			g.setColor(new Color(0, 150, 0));
			Point p = this.predict(1000);
			g.fillRect(p.x - 5, p.y - 5, 10, 10);
		}
		//g.setColor(Color.cyan);
		//g.fillRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
		g.setColor(Color.red);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) rect.width, 3);
		g.setColor(Color.blue);
		g.fillRect((int) rect.x, (int) rect.y - 3, (int) (rect.width * Hp/mHp), 3);
		g.setColor(Color.black);
		g.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 8));
		g.drawString(this.getClass().getSimpleName(), (int) rect.x, (int) rect.y + 26);
		if (Hp <= 0) {
			if (respawns > 0) {
				Hp = mHp;
				respawns --;
				respawn();
			} else {
				Hp = 0;
				Screen.carsToRemove.add(this);
			}
		} else if (Hp > mHp) {
			Hp = mHp;
		}
		if (System.nanoTime() > lastMove + 10000000) {
			lastMove = System.nanoTime() - 10000000;
			moving = false;
		}
		if (System.nanoTime() > lastTurn + 10000000) {
			lastTurn = System.nanoTime() - 10000000;
		}
	}
	public void move(double steps) {
		moving = true;
		moveDir = steps;
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
	public Point predict(int millis) {
		if (moving) {
			double steps = millis/10.0;
			return new Point((int) (rect.getCenterX() + moveDir * steps * Math.cos(theta)), (int) (rect.getCenterY() - moveDir * steps * Math.sin(theta)));
		} else {
			return new Point((int) rect.getCenterX(), (int) rect.getCenterY());
		}
	}
	public void forceMove(double x, double y, double steps) {
		double theta = MathUtils.getAngle(rect, x, y);
		rect.x += steps * Math.cos(theta);
		if (touchingWall()) {
			rect.x -= steps * Math.cos(theta);
		}
		rect.y -= steps * Math.sin(theta);
		if (touchingWall()) {
			rect.y += steps * Math.sin(theta);
		}
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
		Point up = getGridLocation(rect.getCenterX(), rect.y);
		Point left = getGridLocation(rect.x, rect.getCenterY());
		Point down = getGridLocation(rect.getCenterX(), rect.getMaxY());
		Point right = getGridLocation(rect.getMaxX(), rect.getCenterY());
		return PoliceChase.grid[loc.y][loc.x].canCollide || PoliceChase.grid[up.y][up.x].canCollide || PoliceChase.grid[left.y][left.x].canCollide || PoliceChase.grid[down.y][down.x].canCollide || PoliceChase.grid[right.y][right.x].canCollide; 
	}
	public void fire() {
		for (Car c: Screen.cars) {
			if (c instanceof Ammo && ((Ammo) c).from == this) {
				c.fire();
			}
		}
		for (Car c: Screen.carsToAdd) {
			if (c instanceof Ammo && ((Ammo) c).from == this) {
				c.fire();
			}
		}
		try {
			if (System.nanoTime() < lastFire + ammo.getField("fireTime").getLong(null)) {
				return;
			}
			Screen.carsToAdd.add((Car) ammo.getConstructor(Car.class).newInstance(this));
			lastFire = System.nanoTime();
		} catch (Exception e) {
			e.printStackTrace();
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
	public void respawn() {
		Point target = new Point();
		boolean b = true;
		do {
			b = true;
			target.x = (int) (Math.random() * 48) + 1; 
			target.y = (int) (Math.random() * 23) + 1;
			if (MathUtils.distanceTo(new Rect(target.x * 28 + 5, target.y * 35 + 5, 0, 0), rect) < 300) {
				b = false;
			}
			for (Car c: Screen.cars) {
				if (c.team != team && (MathUtils.distanceTo(new Rect(target.x * 28 + 5, target.y * 35 + 5, 0, 0), c.rect) < 380 || MathUtils.rayCast(new Rect(target.x * 28 + 5, target.y * 35 + 5, 0, 0), MathUtils.getAngle(new Rect(target.x * 28 + 5, target.y * 35 + 5, 0, 0), c.rect.getCenterX(), c.rect.getCenterY()), c, null))) {
					b = false;
					break;
				}
			}
		} while (!isAccessible(target.x, target.y) || !b);
		rect.x = target.x * 28 + 5;
		rect.y = target.y * 35 + 5;
		spawnX = target.x * 28 + 5;
		spawnY = target.y * 35 + 5;	
	}
	public static boolean isAccessible(int x, int y) {
		try {
			return !PoliceChase.grid[y][x].canCollide;
		} catch (Exception e) {
			return false;
		}
	}
	public static Point getGridLocation(double x, double y) {
		return new Point((int) (x/28), (int) (y/35));
	}
	public static boolean touchingWall(double x, double y, double radius) {
		Point loc = getGridLocation(x, y);
		Point up = getGridLocation(x, y - radius);
		Point left = getGridLocation(x - radius, y);
		Point down = getGridLocation(x, y + radius);
		Point right = getGridLocation(x + radius, y);
		return PoliceChase.grid[loc.y][loc.x].canCollide || PoliceChase.grid[up.y][up.x].canCollide || PoliceChase.grid[left.y][left.x].canCollide || PoliceChase.grid[down.y][down.x].canCollide || PoliceChase.grid[right.y][right.x].canCollide; 
	}
	public void activate() {
		a.activate(this);
	}
}
