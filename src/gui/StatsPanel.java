package gui;

import java.awt.Color;

import javax.swing.JPanel;

public class StatsPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public static final int STATS_HEIGHT = 40;
	
	//this is a panel at the top of the window, may not end up using it.
	public StatsPanel() {
		this.setSize(Window.WIDTH, STATS_HEIGHT);
		this.setBackground(Color.WHITE);
		this.setLayout(null);
	}
}
