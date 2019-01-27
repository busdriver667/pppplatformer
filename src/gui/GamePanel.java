package gui;

import java.awt.Color;

import javax.swing.JPanel;

import physics.Player;
import physics.Inputs;

//this is where the game will be drawn
//it acts as an interlayer between the frame and the panels the player sees
//communicates with statsPanel and playPanel, contains everything.
public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Inputs input;
	private StatsPanel statsPanel = new StatsPanel();
	private PlayArea playPanel = new PlayArea();
	private Player player;
	
	public GamePanel() {
		this.setRequestFocusEnabled(true);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		//this.add(statsPanel);
		//statsPanel.setLocation(0, 0);
		
		this.add(playPanel);
		playPanel.setLocation(0, 0);
		
		input = new Inputs();
		this.addKeyListener(input);
	}
	
	public void addPlayer(Player player) {
		this.player = player;
		playPanel.addPlayer(player);
	}
	
	public void update() {
		playPanel.repaint();
	}
}
