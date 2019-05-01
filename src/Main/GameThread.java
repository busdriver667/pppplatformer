package Main;

import java.awt.event.KeyEvent;
import java.util.LinkedHashSet;

import physics.Player;
import physics.Inputs;
import physics.Map;
import gui.GamePanel;

public class GameThread extends Thread {
	
	private boolean running;
	private GamePanel gamePanel;
	private static final int WAIT=18; //refresh rate = 18ms
	private Player player;
	private Map map;
	
	//this is where the game actually runs.
	public GameThread(GamePanel gamePanel) {
		this.map = new Map();
		this.map.initLevel(1);
		//init player
		this.player = new Player();
		
		//add player to gamePanel
		this.gamePanel = gamePanel;
		this.gamePanel.addPlayer(player);
		
		//game state = running
		this.running = true;
	}
	
	@Override
	public void run() {
		while(running) {
			
			manageKeys(); //process key presses
			gamePanel.update(); //refresh game
			
			try {
				Thread.sleep(WAIT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void manageKeys() {
		
		//current key presses
		LinkedHashSet<Integer> currentKeys = Inputs.getActiveKeys();
		
		
		//run direction
		if(currentKeys.isEmpty() && !player.getJumping() && !player.getDoubleJumping() && !player.getFalling()){
			player.stop();
		} else {
			//avoid clunky movement when both keys are pressed
			if (currentKeys.contains(KeyEvent.VK_RIGHT)
					& currentKeys.contains(KeyEvent.VK_LEFT)) { 
				for (int k : currentKeys) { //whichever key was first pressed will be the direction maintained by player
					if (k == KeyEvent.VK_RIGHT) {
						player.move(KeyEvent.VK_RIGHT);						
					} else if (k == KeyEvent.VK_LEFT) {
						player.move(KeyEvent.VK_LEFT);						
					}
				}			
			} else if (currentKeys.contains(KeyEvent.VK_RIGHT))
				player.move(KeyEvent.VK_RIGHT);			
			else if (currentKeys.contains(KeyEvent.VK_LEFT))
				player.move(KeyEvent.VK_LEFT);
		}
		
		if(currentKeys.contains(KeyEvent.VK_SPACE) || currentKeys.contains(KeyEvent.VK_UP)) {	
			if (!player.getJumping() && !player.getFalling())
				player.setJumping();
		}	
		
		player.update();
	}
	
	//return player object
	public Player getPlayer() {
		return player;
	}
}