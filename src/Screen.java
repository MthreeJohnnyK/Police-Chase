import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Screen extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5241834608422998589L;
	public static ArrayList<Car> cars = new ArrayList<Car>();
	public static ArrayList<Car> carsToAdd = new ArrayList<Car>();
	public static ArrayList<Car> carsToRemove = new ArrayList<Car>();
	public void paint(Graphics g) {
		g.setColor(Color.white);
    	g.fillRect(0, 0, getWidth(), getHeight());
    	drawMap(g);
    	for (Car car: cars) {
    		car.paint(g);
    	}
    	for (Car car: carsToAdd) {
    		cars.add(car);
    	}
    	carsToAdd.clear();
    	for (Car car: carsToRemove) {
    		cars.remove(car);
    	}
    	carsToRemove.clear();
	}
	public void drawMap(Graphics g) {
		g.setColor(Color.green);
		for (int y = 0; y < PoliceChase.grid.length; y ++) {
    		for (int x = 0; x < PoliceChase.grid[y].length; x ++) {
    			if (PoliceChase.grid[y][x]) {
    				g.fillRect(x * 28, y * 35, 28, 35);
    			} else {
    				continue;
    			}
    			g.setColor(Color.black);
    			try {
    				if (!PoliceChase.grid[y - 1][x]){
    					g.fillRect(x * 28, y * 35, 28, 3);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28, y * 35, 28, 3);
    			}
    			try {
    				if (!PoliceChase.grid[y - 1][x + 1]){
    					g.fillRect(x * 28 + 25, y * 35, 3, 3);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28 + 25, y * 35, 3, 3);
    			}
    			try {
    				if (!PoliceChase.grid[y][x - 1]){
    					g.fillRect(x * 28, y * 35, 3, 35);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28, y * 35, 3, 35);
    			}
    			try {
    				if (!PoliceChase.grid[y + 1][x + 1]){
    					g.fillRect(x * 28 + 25, y * 35 + 32, 3, 3);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28 + 25, y * 35 + 32, 3, 3);
    			}
    			try {
    				if (!PoliceChase.grid[y + 1][x]){
    					g.fillRect(x * 28, y * 35 + 32, 28, 3);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28, y * 35 + 32, 28, 3);
    			}
    			try {
    				if (!PoliceChase.grid[y + 1][x - 1]){
    					g.fillRect(x * 28, y * 35 + 32, 3, 3);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28, y * 35 + 32, 3, 3);
    			}
    			try {
    				if (!PoliceChase.grid[y][x + 1]){
    					g.fillRect(x * 28 + 25, y * 35, 3, 35);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28 + 25, y * 35, 3, 35);
    			}
    			try {
    				if (!PoliceChase.grid[y - 1][x - 1]){
    					g.fillRect(x * 28, y * 35, 3, 3);
    				}
    			} catch (Exception e) {
    				g.fillRect(x * 28, y * 35, 3, 3);
    			}
    			g.setColor(Color.green);
    		}
    	}
	}

}
