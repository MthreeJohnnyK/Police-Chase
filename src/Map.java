import java.awt.Point;

public class Map {
	protected final String map;
	protected final String name;
	protected final Point Police1;
	protected final Point Police2;
	protected final Point Thief;
	private static final Map[] maps = {new Map("##################################################"
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
											+ "##################################################", "The Town", new Point(336, 630), new Point(112, 105), new Point(1344, 805)),
										
			
			
										new Map("##################################################"
											+ "#   #                   ##                   #   #"
											+ "# #   ##  #   #   #   # ## #   #   #   #  ##   # #"
											+ "#     #                 ##                 #     #"
											+ "##                      ##                      ##"
											+ "#                                                #"
											+ "# ##                                          ## #"
											+ "# #                                            # #"
											+ "#                                                #"
											+ "#                                                #"
											+ "# ###                                        ### #"
											+ "#                                                #"
											+ "#    ###                                  ###    #"
											+ "#                                                #"
											+ "# ###                                        ### #"
											+ "#                                                #"
											+ "#                                                #"
											+ "# #                                            # #"
											+ "# ##                                          ## #"
											+ "#                                                #"
											+ "##                      ##                      ##"
											+ "#     #                 ##                 #     #"
											+ "# #   ##  #   #   #   # ## #   #   #   #  ##   # #"
											+ "#   #                   ##                   #   #"
											+ "##################################################", "The Plains", new Point(28, 805), new Point(28, 35), new Point(1344, 805))};
	public Map(String map, String name, Point Police1, Point Police2, Point Thief) {
		this.map = map;
		this.name = name;
		this.Police1 = Police1;
		this.Police2 = Police2;
		this.Thief = Thief;
	}
	public MapObject[][] synthesis() {
		MapObject[][] grid = new MapObject[25][50];
		for (int y = 0; y < 25; y ++) {
        	for (int x = 0; x < 50; x ++) {
        		grid[y][x] = map.charAt(y * 50 + x) == '#' ? new Wall(x, y) : new Air(x, y);
        	}
        }
		return grid;
	}
	public static Map generate() {
		return maps[(int) (Math.random() * maps.length)];
	}
	public static Map generate(int index) {
		return maps[index >= maps.length ? maps.length - 1 : index];
	}
}
