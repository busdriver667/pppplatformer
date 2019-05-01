package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import physics.Map;
import physics.Player;

//this class draws the game and is what the player sees
public class PlayArea extends JPanel{

	private static final long serialVersionUID = 1L;
	
	//public static final int PLAY_PANEL_HEIGHT = Window.HEIGHT;
	public static final int TILESIZE = 64;
	private Player player;
	private int mapOffset = 0;
	private int playerOffset = 0;
	public PlayArea() {
		//set size of play panel
		this.setSize(Window.WIDTH, Window.HEIGHT);
		this.setLocation(0, 0);
		this.setLayout(null);
		
		//improves animations http://www.anandtech.com/show/2794/2
		this.setDoubleBuffered(true);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//anti-aliasing for smoother lines
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
		g2.drawImage(Map.BG, 0, 0, null);
		
		mapOffset = player.getX() / TILESIZE;
		System.out.println(mapOffset);
		if (player.getX() > (Window.WIDTH / 2)) {
			Map.updateMap(mapOffset - 1);
			playerOffset = player.getX() - (Window.WIDTH / 2);
		} else {
			Map.updateMap(0);
			playerOffset = 0;
		}
		
		//draw blocks
		for (int i = 0; i < Map.ROWS; i++) {
			for (int j = 0; j < Map.displayedMap[1].length; j++) {
				if (Map.displayedMap[i][j] != null) {
					g2.drawImage(Map.displayedMap[i][j].getImage(), Map.displayedMap[i][j].getX() % 1280, Map.displayedMap[i][j].getY(), null);
				}
			}
		}
		
		for (int i = 0; i < Map.ROWS; i++) {
			for (int j = 0; j < Map.COLUMNS; j++) {
				g2.drawLine(0, i * TILESIZE, Window.WIDTH, i * TILESIZE);
				g2.drawLine(j * TILESIZE, 0, j * TILESIZE, Window.HEIGHT);
			}
		}
		
		g2.drawImage(player.getCurrentFrame(), player.getX() - player.XOFFSET - playerOffset, player.getY() , null);
	}
	
	//this is called by gameThread to add the player to the drawn play area
	public void addPlayer(Player player) {
		this.player = player;
	}
}
