package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import physics.Player;

//this class draws the game and is what the player sees
public class PlayArea extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public static final int TERRAIN_HEIGHT = 120; //distance from ground tile to bottom of window
	public static final int PLAY_PANEL_HEIGHT = Window.HEIGHT;
	public static final int TILESIZE = 60;
	private Player player;
	
	public PlayArea() {
		//set size of play panel
		this.setSize(Window.WIDTH, PLAY_PANEL_HEIGHT);
		
		//separate play area from everything else 
		this.setBackground(Color.DARK_GRAY);
		
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
		
		//grid of tiles - temporary
		
		for (int i = 0; i < Window.WIDTH / TILESIZE; i++) {
			g2.drawLine(0,  i * TILESIZE, Window.WIDTH, i * TILESIZE);
			g2.drawLine(i * TILESIZE, 0, i * TILESIZE, Window.HEIGHT);
		}
		
		//draw player
		g2.drawImage(player.getCurrentFrame(), player.getX(), player.getY(), null);
	}
	
	//this is called by gameThread to add the player to the drawn play area
	public void addPlayer(Player player) {
		this.player = player;
	}
}
