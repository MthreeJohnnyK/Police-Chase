

import java.awt.Dimension; 
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PoliceChase {
    //detection of user screen resolution 
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static double width = screenSize.getWidth();
    static double height = screenSize.getHeight();
    static Car Police1;
    static Car Police2;
    static Car Thief;
    //sets up fps calculation 
    static int frames = 0;
    static long lastUpdate = System.nanoTime();
    static long lastSound = System.currentTimeMillis();
    static long startTime = System.currentTimeMillis();
    private static long lastNavigate = System.currentTimeMillis() - 1000;
    static double fps = 15.0;
    static BufferedImage img;
    static JPanel s;// = new Screen(1, 26, new Basic(Screen.X(500), Screen.Y(880)));
    static JPanel queue;
    static boolean devMode = true;
    static Map map;
    static MapObject[][] grid;
    //initalizes the frame
    static JFrame frame = new JFrame();
    public static void main(String[] args) {
    	Assets.loadImages();
    	s = new Screen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) width, (int) height);
        frame.setLocation(0, 0);
        frame.setName("Police Chase");
        frame.setTitle("Police Chase");
        System.out.println(System.getProperty("user.name"));
        URL resourceURL = PoliceChase.class.getResource("images/Blue.png"); 
        System.out.println(resourceURL);
        try {
            //img = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\Birb.png"));
            img = ImageIO.read(resourceURL);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        frame.setIconImage(img);
        //frame.addMouseListener(new Mouse());
        map = Map.generate(1);
        grid = map.synthesis();
        PowerUp.newPowerUp();
        Thief = new Thief(map.Thief.x, map.Thief.y, 0, Gas.class, new Drone());
        Thief.devMode = false;
        Screen.carsToAdd.add(Thief);
        frame.addKeyListener((KeyListener) Thief);
        Police1 = new Penguin(map.Police1.x, map.Police1.y, 0, Cannonball.class, new Heal()); 
        Police1.devMode = false;
        Screen.carsToAdd.add(Police1);
        frame.addKeyListener((KeyListener) Police1);
        Police2 = new Penguin(map.Police2.x, map.Police2.y, 0, Lazer.class, new Heal());
        Police2.devMode = false;
        Screen.carsToAdd.add(Police2);
        frame.addKeyListener((KeyListener) Police2);
        //initalizes the screen
        frame.add(s);
        frame.setVisible(true);
        //main game loop
        while (true) { 
        	//width = frame.getWidth();
        	//height = frame.getHeight();
              //updates fps every 166 milliseconds
        	if (System.nanoTime() >= lastUpdate + 166000000L) {
                //calculates fps
                fps = frames/((System.nanoTime() - lastUpdate)/1000000000.0);
                //System.out.println(fps);
                lastUpdate = System.nanoTime(); 
                frames = 0;
        	}
              if (System.currentTimeMillis() >= lastSound + 100) {
                  //calculates fps
                  //Assets.playAll();
                  lastSound = System.currentTimeMillis(); 
              }
              //paints the screen
              s.repaint();
              frames ++;
              
              //fps limit of 30 fps (waits 33 milliseconds before painting next screen)
              try {
            	  /*if (s instanceof Start) {
            		  Thread.sleep(100);
            	  } else {*/
            		  Thread.sleep(fps > 150 ? 6 : 5);
            	  //}
              } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
              } 
              if (queue != null) {
            	  frame.getContentPane().removeAll();
            	  frame.add(queue);
            	  //updates screen
                  frame.setVisible(true);
            	  PoliceChase.s = queue;
            	  queue = null;
              }
              //System.out.println(frame.getComponentCount());
         }
    }
    public static void navigate(JPanel from, JPanel to) {
    	if (System.currentTimeMillis() - 1000 >= lastNavigate) {
    		lastNavigate = System.currentTimeMillis();
    		from.removeAll();
    		queue = queue == null ? to : queue;
    		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	    width = screenSize.getWidth();
    	    height = screenSize.getHeight();
    	}
    }
}
