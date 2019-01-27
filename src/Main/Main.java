package Main;

import gui.Window;
import gui.GamePanel;

public class Main {

	public static void main(String []args) {
		//init GamePanel
		GamePanel gamePanel = new GamePanel();
		
		//init thread
		GameThread gameThread = new GameThread(gamePanel);
		gameThread.start();
		
		//init frame
		Window gameFrame = new Window(gamePanel);
	}
}
