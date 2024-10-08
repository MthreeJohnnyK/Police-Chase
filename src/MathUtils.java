import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class MathUtils {
	public static float sin(float theta) {
		theta = (float) (((theta + Math.PI) %  (2 * Math.PI)) - Math.PI);
		return (float) (theta - (theta * theta * theta)/6f + (theta * theta * theta * theta * theta)/120f - (theta * theta * theta * theta * theta * theta * theta)/5040f);
	}
	public static float cos(float theta) {
		theta = (float) (((theta + Math.PI) % (2 * Math.PI)) - Math.PI);
		return (float) (1 - (theta * theta)/2f + (theta * theta * theta * theta)/24f - (theta * theta * theta * theta * theta * theta)/720f + (theta * theta * theta * theta * theta * theta * theta * theta)/40320f);
	}
	public static float tan(float theta) {
		return sin(theta) / cos(theta);
	}
	public static double getAngle(Rect from, double x, double y) {
		double dx = x - from.getCenterX();
		double dy = -1 * (y - from.getCenterY());
		if (dx == 0 && dy > 0) {
			return Math.PI/2;
		} else if (dx == 0 && dy < 0) {
			return 3 * Math.PI/2;
		}
		double dir = (float) Math.atan(dy/dx);
		if (dx > 0) {
			dir += 2 * Math.PI;
		} else if (dx < 0) {
			dir += Math.PI;
		}
		return dir %= 2 * Math.PI;
	}
	public static boolean isAngleClose(double x, double y, double tolerance) {
		return angularDistance(x, y) <= tolerance;
	}
	public static boolean findClosestDir(double x, double y) {
		return angularDistance((x + 0.001) % (2 * Math.PI), y) < angularDistance((x - 0.001 + 2 * Math.PI) % (2 * Math.PI), y);
	}
	public static double angularDistance(double x, double y) {
		double dist = x - y;
		dist = Math.abs(dist > Math.PI ? 2 * Math.PI - dist : dist);
		return dist > Math.PI ? 2 * Math.PI - dist : dist;
	}
	public static boolean rayCast(Rect rect, double theta, Object target, Graphics g){
		Rect r = new Rect(rect.getCenterX(), rect.getCenterY(), 1, 1);
		for (int count = 0; true; count++) {
			r.x += Math.cos(theta);
			r.y -= Math.sin(theta);
			if (Car.touchingWall(r.x, r.y, count < 50 ? 13 : 3)) {
				if (g != null) {
					g.setColor(Color.red);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return false;
			}
			if ((target instanceof Car && r.intersects(((Car) target).rect)) || (target instanceof Rect && r.intersects((Rect) target))) {
				if (g != null) {
					g.setColor(Color.green);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return true;
			}
		}
	}
	public static boolean rayCast(Rect rect, double theta, Object target, int rayWidth, Graphics g){
		Rect r = new Rect(rect.getCenterX(), rect.getCenterY(), 1, 1);
		while (true) {
			r.x += Math.cos(theta);
			r.y -= Math.sin(theta);
			if (Car.touchingWall(r.x, r.y, rayWidth)) {
				if (g != null) {
					g.setColor(Color.red);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return false;
			}
			if ((target instanceof Car && r.intersects(((Car) target).rect)) || (target instanceof Rect && r.intersects((Rect) target))) {
				if (g != null) {
					g.setColor(Color.green);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return true;
			}
		}
	}
	public static boolean rayCast(Rect rect, double theta, Object target, boolean ignore, int limit, Graphics g){
		Rect r = new Rect(rect.getCenterX(), rect.getCenterY(), 1, 1);
		for (int count = 0; true; count++) {
			r.x += Math.cos(theta);
			r.y -= Math.sin(theta);
			if (!ignore && Car.touchingWall(r.x, r.y, count < 50 ? 13 : 3)) {
				if (g != null) {
					g.setColor(Color.red);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return false;
			}
			if (r.x < 28 || r.y < 35 || r.x > 1372 || r.y > 840 || count >= limit) {
				if (g != null) {
					g.setColor(Color.red);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return false;
			}
			if ((target instanceof Car && r.intersects(((Car) target).rect)) || (target instanceof Rect && r.intersects((Rect) target))) {
				if (g != null) {
					g.setColor(Color.green);
					g.drawLine((int) rect.getCenterX(), (int) rect.getCenterY(), (int) r.x, (int) r.y);
				}
				return true;
			}
		}
	}
	public static double distanceTo(Rect rect, Rect other){
		return Math.sqrt(Math.pow(rect.getCenterX() - other.getCenterX(), 2) + Math.pow(rect.getCenterY() - other.getCenterY(), 2));
	}
}
