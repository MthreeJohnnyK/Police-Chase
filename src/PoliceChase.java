

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
    static String gridString = "##################################################"
    		+ "#                                                #"
    		+ "#         #                      #    #    #     #"
    		+ "#    #          #     ##              #          #"
    		+ "#         #     #      ##           #####      ###"
    		+ "#         #     #       ##            #          #"
    		+ "#   #######     #        ##      #    #    #     #"
    		+ "#              ##         ##                     #"
    		+ "#             ##           #                     #"
    		+ "######       ##               #######     #    ###"
    		+ "#           ##                #           #      #"
    		+ "#          ##                 #           #      #"
    		+ "#      #####        #         #     #######      #"
    		+ "###                                            ###"
    		+ "###                                            ###"
    		+ "#      #####           #      #########   #      #"
    		+ "#      #         #                #       #      #"
    		+ "#    ###         ##               #       #      #"
    		+ "#    ###     #    ##        ###   #   ########   #"
    		+ "#      #           ##                 #          #"
    		+ "#      #            ##                           #"
    		+ "###    #    #####    ##       #            #     #"
    		+ "###                       #   #########          #"
    		+ "#                         #                      #"
    		+ "##################################################";
    //sets up fps calculation 
    static int frames = 0;
    static long lastUpdate = System.currentTimeMillis();
    static long lastSound = System.currentTimeMillis();
    static long startTime = System.currentTimeMillis();
    private static long lastNavigate = System.currentTimeMillis() - 1000;
    static double fps = 15.0;
    static BufferedImage img;
    static JPanel s;// = new Screen(1, 26, new Basic(Screen.X(500), Screen.Y(880)));
    static JPanel queue;
    static boolean devMode = true;
    static boolean[][] grid = new boolean[25][50];
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
        frame.setVisible(true);
        //initalizes the screen
        frame.add(s);
        System.out.println(gridString.length());
        for (int y = 0; y < 25; y ++) {
        	for (int x = 0; x < 50; x ++) {
        		grid[y][x] = gridString.charAt(y * 50 + x) == '#';
        	}
        }
        Thief = new Thief(1344, 805, 0, Missile.class);
        Screen.carsToAdd.add(Thief);
        frame.addKeyListener((KeyListener) Thief);
        Police1 = new Police1(336, 630, 0, Bomb.class);
        Screen.carsToAdd.add(Police1);
        frame.addKeyListener((KeyListener) Police1);
        Police2 = new Penguin(112, 105, 0, Minigun.class);
        Screen.carsToAdd.add(Police2);
        //main game loop
        while (true) { 
        	//width = frame.getWidth();
        	//height = frame.getHeight();
              //updates fps every 166 milliseconds
              if (System.currentTimeMillis() >= lastUpdate + 166) {
                      //calculates fps
                      fps = frames/((System.currentTimeMillis() - lastUpdate)/1000.0);
                      lastUpdate = System.currentTimeMillis(); 
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
            		  Thread.sleep(6);
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
