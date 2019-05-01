package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L; //yes
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720; // 64 * number of rows
	
	public Window(GamePanel gamePanel) {
		
		this.setResizable(false);
		this.add(gamePanel);
		this.setLocation(0, 0);
		//set window location to the middle of the screen	
		this.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - WIDTH) / 2),
				((int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - HEIGHT) / 2));
		
		
		//window resolution
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		this.setVisible(true);
		
		gamePanel.grabFocus();
		gamePanel.requestFocusInWindow();
	}
}
