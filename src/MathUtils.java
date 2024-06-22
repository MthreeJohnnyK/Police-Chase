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
	public static boolean rayCast(Rect rect, double theta, Car target){
		Rect r = new Rect(rect.x, rect.y, 1, 1);
		while(true) {
			r.x += Math.cos(theta);
			r.y -= Math.sin(theta);
			if (Car.touchingWall(Car.getGridLocation(r.x, r.y))) {
				return false;
			}
			if (r.intersects(target.rect)) {
				return true;
			}
		}
	}
	public static double distanceTo(Rect rect, Rect other){
		return Math.sqrt(Math.pow(rect.x - other.x, 2) + Math.pow(rect.y - other.y, 2));
	}
}
