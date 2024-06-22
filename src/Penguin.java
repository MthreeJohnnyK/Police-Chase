import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Penguin extends Car {
	private Point target = PoliceChase.Thief.getGridLocation();
	private Point curTarget = getGridLocation();
	private int res;
	private boolean devMode = false;
	int[][] grid = new int[25][50];
	public Penguin(int x, int y, double theta, Class ammo) {
		super(x, y, theta, 0.44, 10, Assets.newImage("Blue.png"), ammo, true);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		Car closest = null;
		double distance = Double.MAX_VALUE;
		for (Car c: Screen.cars) {
			if (!(c instanceof Ammo) && c.team != team && MathUtils.distanceTo(rect, c.rect) < distance) {
				closest = c;
			}
		}
		if (closest == null) {
			System.out.println("The Police Wins!");
			System.exit(0);
		} 
		target = closest.getGridLocation();
		if (curTarget.equals(getGridLocation())) {
			curTarget = getGridLocation();
			res = pathFind(g);
			switch(res) {
				case 1:
					curTarget.x ++;
					break;
				case 2:
					curTarget.x ++;
					curTarget.y --;
					break;
				case 3:
					curTarget.y --;
					break;
				case 4:
					curTarget.y --;
					curTarget.x --;
					break;
				case 5:
					curTarget.x --;
					break;
				case 6:
					curTarget.x --;
					curTarget.y ++;
					break;
				case 7:
					curTarget.y ++;
					break;
				case 8: case 0:
					curTarget.y ++;
					curTarget.x ++;
					break;
			}
		}
		double tarAngle = MathUtils.getAngle(rect, curTarget.x * 28 + 14, curTarget.y * 35 + 17.5);
		double thiefAngle = MathUtils.getAngle(rect, closest.rect.getCenterX(),  closest.rect.getCenterY());
		//System.out.println(tarAngle);
		if (!MathUtils.rayCast(rect, thiefAngle, closest, devMode ? g : null)) {
			if (MathUtils.isAngleClose(getRadians(), tarAngle, Math.PI/18)) {
			} else if (MathUtils.findClosestDir(getRadians(), tarAngle)){
				turn(speed * 0.6);
			} else {
				turn(-speed * 0.6);
			}
			if (MathUtils.isAngleClose(getRadians(), tarAngle, Math.PI/4)) {
				move(speed);
			}
		} else {
			if (MathUtils.isAngleClose(getRadians(), thiefAngle, Math.PI/180)) {
				fire();
			} else if (MathUtils.findClosestDir(getRadians(), thiefAngle)){
				turn(Math.PI/360);
			} else {
				turn(-Math.PI/360);
			}
		}
		if (devMode) {
			g.setColor(Color.red);
			g.drawRect(curTarget.x * 28, curTarget.y * 35, 28, 35);
			g.setColor(Color.cyan);
			g.drawRect(target.x * 28, target.y * 35, 28, 35);
			g.setColor(Color.green);
			g.drawRect(getGridLocation().x * 28, getGridLocation().y * 35, 28, 35);
			g.setColor(Color.black);
			g.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 26));
			for (int y = 0; y < grid.length; y ++) {
				for (int x = 0; x < grid[y].length; x ++) {
					if (grid[y][x] != 0) {	
						g.drawString(grid[y][x] + "", x * 28 + 10, y * 35 + 25);
					}
				}
			}
			if (getGridLocation().equals(target)) {
				target = getRandomLocation();
			}
		}  	
	}
	public int pathFind(Graphics g) {
		grid = new int[25][50];
	    ArrayList<Point> queue = new ArrayList<Point>();
	    Point p = getGridLocation();
	    int x = p.x;
	    int y = p.y;
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
	    for (var i = 0; i < queue.size(); i++) {
	        var curX = queue.get(i).x;
	        var curY = queue.get(i).y;
	        if (curX < 1 || curX > 48 || curY < 1 || curY > 23) {
	            continue;
	        }
	        if (curX == target.x && curY == target.y) {
	            return grid[curY][curX];
	        }
	        if (grid[curY][curX - 1] == 0 && isAccessible(curX - 1, curY)) {
	            grid[curY][curX - 1] = grid[curY][curX];
	            queue.add(new Point(curX - 1, curY));
	        }
	        if (grid[curY - 1][curX] == 0 && isAccessible(curX, curY - 1)) {
	            grid[curY - 1][curX] = grid[curY][curX];
	            queue.add(new Point(curX, curY - 1));
	        }
	        if (grid[curY][curX + 1] == 0 && isAccessible(curX + 1, curY)) {
	            grid[curY][curX + 1] = grid[curY][curX];
	            queue.add(new Point(curX + 1, curY));
	        }
	        if (grid[curY + 1][curX] == 0 && isAccessible(curX, curY + 1)) {
	            grid[curY + 1][curX] = grid[curY][curX];
	            queue.add(new Point(curX, curY + 1));
	        }
	        if (grid[curY - 1][curX + 1] == 0 && isAccessible(curX + 1, curY - 1) && grid[curY][curX] == 2) {
	            grid[curY - 1][curX + 1] = grid[curY][curX];
	            queue.add(new Point(curX + 1, curY - 1));
	        }
	        if (grid[curY - 1][curX - 1] == 0 && isAccessible(curX - 1, curY - 1) && grid[curY][curX] == 4) {
	            grid[curY - 1][curX - 1] = grid[curY][curX];
	            queue.add(new Point(curX - 1, curY - 1));
	        }
	        if (grid[curY + 1][curX - 1] == 0 && isAccessible(curX - 1, curY + 1) && grid[curY][curX] == 6) {
	            grid[curY + 1][curX - 1] = grid[curY][curX];
	            queue.add(new Point(curX - 1, curY + 1));
	        }
	        if (grid[curY + 1][curX + 1] == 0 && isAccessible(curX + 1, curY + 1) && grid[curY][curX] == 8) {
	            grid[curY + 1][curX + 1] = grid[curY][curX];
	            queue.add(new Point(curX + 1, curY + 1));
	        }
	    }
	    return 0;
	}
}
