import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Penguin extends Car implements KeyListener{
	private Point target = getGridLocation();
	protected Point curTarget = getGridLocation();
	private Point last = null;
	private Point thiefLast = null;
	private long lastPathFind = System.nanoTime();
	int[][] grid = new int[25][50];
	public Penguin(int x, int y, double theta, Class ammo, Ability a) {
		this(x, y, theta, ammo, a, true);
		// TODO Auto-generated constructor stub
	}
	public Penguin(int x, int y, double theta, Class ammo, Ability a, boolean team) {
		super(x, y, theta, 0.5, team ? 10 : 25, Assets.newImage(team ? "Blue.png" : "Red.png"), ammo, a, team);
		if (!(a == null || (a instanceof Heal) || (a instanceof RapidFire))) {
			throw new IllegalArgumentException("Ability Unavailable");
		}
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		Car closest = null;
		target = null;
		double distance = Double.MAX_VALUE;
		double pri = Double.MAX_VALUE;
		boolean ignore;
		boolean directPath = false;
		try {
			ignore = ammo.getField("penetrable").getBoolean(null);
		} catch (Exception e) {
			ignore = false;
		}
		int limit;
		try {
			limit = ammo.getField("limit").getInt(null);
		} catch (Exception e) {
			limit = Integer.MAX_VALUE;
		}
		for (Car c: Screen.cars) {
			if (c instanceof Ammo || c instanceof Trap && ((Trap) c).visibility <= 0 || c.team == team) {
				continue;
			}
			double d = MathUtils.distanceTo(rect, c.rect);
			if (MathUtils.rayCast(rect, MathUtils.getAngle(rect, c.rect.getCenterX(), c.rect.getCenterY()), c, ignore, limit, devMode ? g : null)) {
				d /= 10;
				directPath = true;
			} else if (d < pri){	
				directPath = false;
			}
			if (d < pri /*&& !Car.touchingWall(c.rect.getCenterX(), c.rect.getCenterY(), 0)*/) {
				closest = c;
				distance = MathUtils.distanceTo(rect, c.rect);
				pri = d;
			}
		}
		if (!(this instanceof Minicar) && !directPath) {
			for (PowerUp p: Screen.powerups) {
				if (MathUtils.distanceTo(rect, new Rect(p.x * 28, p.y * 35, 28 , 35)) < distance && p.pickupCheck(this)) {
					distance = MathUtils.distanceTo(rect, new Rect(p.x * 28, p.y * 35, 28 , 35));
					target = new Point(p.x, p.y);
				}
			}
		}
		if (closest == null) {
			closest = this;
			target = getGridLocation();
		} else if (target == null){
			target = closest.getGridLocation();
		}
		if (a != null && a instanceof Heal && (Hp <= mHp - 3 || Hp < mHp && Hp > mHp - 3 && distance > 500)) {
			activate();
		} 
		if (last == null || thiefLast == null || !getGridLocation().equals(last) || !target.equals(thiefLast) || System.nanoTime() >= lastPathFind + 300000000) {
			curTarget = pathFind(g);
			last = getGridLocation();
			thiefLast = target;
			lastPathFind = System.nanoTime();
		}
		double tarAngle = MathUtils.getAngle(rect, curTarget.x * 28 + 14, curTarget.y * 35 + 17.5);
		//System.out.println(tarAngle);
		try {
			double speed;
			try {
				speed = ammo.getField("speed").getDouble(null);
			} catch (Exception e) {
				speed = 1.5;
			}
			Rect p = predictCalc(distance, speed, closest);
			if (devMode) {
				g.setColor(Color.green);
				g.fillRect((int) p.x, (int) p.y, (int) p.width, (int) p.height);
			}
			double thiefAngle = MathUtils.getAngle(rect, p.getCenterX(),  p.getCenterY());
			boolean inRange = distance < ammo.getField("preferredRange").getInt(null);
			boolean canFire = System.nanoTime() > lastFire + ammo.getField("fireTime").getLong(null);
			if (MathUtils.rayCast(rect, thiefAngle, p, false, limit, devMode ? g : null)) {
				tarAngle = thiefAngle;
			}
			if (!inRange) {
				if (directPath && canFire) {
					aim(thiefAngle, closest, p, ignore);
				} else {
					moveToTarget(tarAngle);
				}
			} else if (!directPath) {
				moveToTarget(tarAngle);
			} else {
				aim(thiefAngle, closest, p, ignore);
			} 
		} catch(Exception e) {
			moveToTarget(tarAngle);
		}
		if (devMode) {
			g.setColor(Color.red);
			g.drawRect(curTarget.x * 28, curTarget.y * 35, 28, 35);
			g.setColor(new Color(150, 0, 150));
			g.drawRect(target.x * 28, target.y * 35, 28, 35);
			g.setColor(new Color(0, 150, 0));
			g.drawRect(getGridLocation().x * 28, getGridLocation().y * 35, 28, 35);
			g.setColor(Color.black);
			for (int y = 0; y < grid.length; y ++) {
				for (int x = 0; x < grid[y].length; x ++) {
					switch(grid[y][x]) {
	            		case 1: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 - 14, y * 35 + 18);
	            			break;
	            		case 3: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 + 14, y * 35 + 53);
	            			break;
	            		case 5: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 + 42, y * 35 + 18);
	            			break;
	            		case 7: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 + 14, y * 35 - 17);
	            			break;
	            		case 2: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 - 14, y * 35 + 53);
	            			break;
	            		case 4: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 + 42, y * 35 + 53);
	            			break;
	            		case 6: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 + 42, y * 35 - 17);
	            			break;
	            		case 8: 
	            			g.drawLine(x * 28 + 14, y * 35 + 18, x * 28 - 14, y * 35 - 17);
	            			break;
					}
				}
			}
			g.setColor(new Color(0, 150, 255));
			Point res = thiefLast;
        	Point next = thiefLast;
            while (!next.equals(last)) {
            	res = next;
            	g.setColor(res.equals(curTarget) ? Color.orange : g.getColor());
	            switch(grid[res.y][res.x]) {
            		case 1: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 - 14, res.y * 35 + 18);
            			next = new Point(res.x - 1, res.y);
            			break;
            		case 3: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 + 14, res.y * 35 + 53);
            			next = new Point(res.x, res.y + 1);
            			break;
            		case 5: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 + 42, res.y * 35 + 18);
            			next = new Point(res.x + 1, res.y);
            			break;
            		case 7: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 + 14, res.y * 35 - 17);
            			next = new Point(res.x, res.y - 1);
            			break;
            		case 2: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 - 14, res.y * 35 + 53);
            			next = new Point(res.x - 1, res.y + 1);
            			break;
            		case 4: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 + 42, res.y * 35 + 53);
            			next = new Point(res.x + 1, res.y + 1);
            			break;
            		case 6: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 + 42, res.y * 35 - 17);
            			next = new Point(res.x + 1, res.y - 1);
            			break;
            		case 8: 
            			g.drawLine(res.x * 28 + 14, res.y * 35 + 18, res.x * 28 - 14, res.y * 35 - 17);
            			next = new Point(res.x - 1, res.y - 1);
            			break;
	            }
            }
            g.setColor(new Color(0, 150, 255));
			g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), curTarget.x * 28 + 14, curTarget.y * 35 + 18);
		}  	
	}
	public void moveToTarget(double tarAngle) {
		if (MathUtils.isAngleClose(getRadians(), tarAngle, Math.PI/36 * speed)) {
		} else if (MathUtils.findClosestDir(getRadians(), tarAngle)){
			turn(speed * 0.05);
		} else {
			turn(-speed * 0.05);
		}
		if (MathUtils.isAngleClose(getRadians(), tarAngle, Math.PI/4)) {
			move(speed);
		}
	}
	public void aim(double thiefAngle, Car c, Rect predict, boolean ignore) {
		double tol = tolCalc(predict, ignore);
		if (tol == 0) {
			return;
		} else if (MathUtils.isAngleClose(getRadians(), thiefAngle, tol)) {
			fire();
			if (ammo == Missile.class) {
				fire();
			}
			if (a != null && a instanceof RapidFire && c.Hp >= 5) {
				activate();
			}
		} else if (MathUtils.findClosestDir(getRadians(), thiefAngle)){
			turn(MathUtils.angularDistance(thiefAngle, theta) < speed * 0.05 ? tol/1.5 : speed * 0.05);
		} else if (!MathUtils.findClosestDir(getRadians(), thiefAngle)){
			turn(MathUtils.angularDistance(thiefAngle, theta) < speed * 0.05 ? -tol/1.5 : -speed * 0.05);
		}
	}
	public void respawn() {
		super.respawn();
		curTarget = getGridLocation();
	}
	private Rect predictCalc(double distance, double speed, Car c) {
		Point p = c.predict((int) (distance/speed * 8));
		Rect r = new Rect(p.x - 10, p.y - 10, 20, 20);
		double dist = MathUtils.distanceTo(rect, r);
		if (dist > distance + 10) {
			return predictCalc(dist, speed, c);
		} else if (dist < distance - 10) {
			return predictCalc(dist, speed, c);
		} else {
			return r;
		}
	}
	public double tolCalc(Rect r, boolean ignore) {
		int w = -1;
		for (int width = (int) (Math.min(r.width, r.height)/2); width > 1 && !ignore; width --) {
			if (MathUtils.rayCast(rect, MathUtils.getAngle(rect, r.getCenterX(), r.getCenterY()), r, width, null)) {
				w = width;
				break;
			}
		}
		w = ignore ? (int) (Math.min(r.width, r.height)/2) : w;
		if (w != -1) {
			return Math.atan(w/MathUtils.distanceTo(rect, r));
		} else {
			return 0;
		}
	}
	private Point pathFind(Graphics g) {
		grid = new int[25][50];
	    ArrayList<Point> queue = new ArrayList<Point>();
	    Point p = getGridLocation();
	    int x = p.x;
	    int y = p.y;
	    if (isAccessible(x + 1, y)) {
	        grid[y][x + 1] = 1;
	        queue.add(new Point(x + 1, y));
	    }
	    if (isAccessible(x, y - 1)) {
	        grid[y - 1][x] = 3;
	        queue.add(new Point(x, y - 1));
	    }
	    if (isAccessible(x - 1, y)) {
	        grid[y][x - 1] = 5;
	        queue.add(new Point(x - 1, y));
	    } 
	    if (isAccessible(x, y + 1)) {
	        grid[y + 1][x] = 7;
	        queue.add(new Point(x, y + 1));
	    }
	    if (isAccessible(x + 1, y - 1)) {
	        grid[y - 1][x + 1] = 2;
	        queue.add(new Point(x + 1, y - 1));
	    }
	    if (isAccessible(x - 1, y - 1)) {
	        grid[y - 1][x - 1] = 4;
	        queue.add(new Point(x - 1, y - 1));
	    }
	    if (isAccessible(x - 1, y + 1)) {
	        grid[y + 1][x - 1] = 6;
	        queue.add(new Point(x - 1, y + 1));
	    }
	    if (isAccessible(x + 1, y + 1)) {
	        grid[y + 1][x + 1] = 8;
	        queue.add(new Point(x + 1, y + 1));
	    }
	    for (var i = 0; i < queue.size(); i++) {
	        var curX = queue.get(i).x;
	        var curY = queue.get(i).y;
	        if (curX < 1 || curX > 48 || curY < 1 || curY > 23) {
	            continue;
	        }
	        if (curX == target.x && curY == target.y) {
	        	Point res = new Point(curX, curY);
	        	Point next = new Point(curX, curY);
	            while (!next.equals(getGridLocation())) {
	            	res = next;
	            	if (MathUtils.rayCast(rect, MathUtils.getAngle(rect, res.x * 28+ 14, res.y * 35 + 17.5), new Rect(res.x * 28, res.y * 35, 28, 35), null)) {
	            		return res;
	            	}
		            switch(grid[res.y][res.x]) {
	            		case 1: 
	            			next = new Point(res.x - 1, res.y);
	            			break;
	            		case 3: 
	            			next = new Point(res.x, res.y + 1);
	            			break;
	            		case 5: 
	            			next = new Point(res.x + 1, res.y);
	            			break;
	            		case 7: 
	            			next = new Point(res.x, res.y - 1);
	            			break;
	            		case 2: 
	            			next = new Point(res.x - 1, res.y + 1);
	            			break;
	            		case 4: 
	            			next = new Point(res.x + 1, res.y + 1);
	            			break;
	            		case 6: 
	            			next = new Point(res.x + 1, res.y - 1);
	            			break;
	            		case 8: 
	            			next = new Point(res.x - 1, res.y - 1);
	            			break;
		            }
	            }
	            return res;
	        }
	        if (grid[curY][curX - 1] == 0 && isAccessible(curX - 1, curY)) {
	            grid[curY][curX - 1] = 5;
	            queue.add(new Point(curX - 1, curY));
	        }
	        if (grid[curY - 1][curX] == 0 && isAccessible(curX, curY - 1)) {
	            grid[curY - 1][curX] = 3;
	            queue.add(new Point(curX, curY - 1));
	        }
	        if (grid[curY][curX + 1] == 0 && isAccessible(curX + 1, curY)) {
	            grid[curY][curX + 1] = 1;
	            queue.add(new Point(curX + 1, curY));
	        }
	        if (grid[curY + 1][curX] == 0 && isAccessible(curX, curY + 1)) {
	            grid[curY + 1][curX] = 7;
	            queue.add(new Point(curX, curY + 1));
	        }
	        if (grid[curY - 1][curX + 1] == 0 && isAccessible(curX + 1, curY - 1)) {
	            grid[curY - 1][curX + 1] = 2;
	            queue.add(new Point(curX + 1, curY - 1));
	        }
	        if (grid[curY - 1][curX - 1] == 0 && isAccessible(curX - 1, curY - 1)) {
	            grid[curY - 1][curX - 1] = 4;
	            queue.add(new Point(curX - 1, curY - 1));
	        }
	        if (grid[curY + 1][curX - 1] == 0 && isAccessible(curX - 1, curY + 1)) {
	            grid[curY + 1][curX - 1] = 6;
	            queue.add(new Point(curX - 1, curY + 1));
	        }
	        if (grid[curY + 1][curX + 1] == 0 && isAccessible(curX + 1, curY + 1)) {
	            grid[curY + 1][curX + 1] = 8;
	            queue.add(new Point(curX + 1, curY + 1));
	        }
	    }
	    return new Point(x, y);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
