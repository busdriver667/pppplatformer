package physics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.LinkedHashSet;

//the key presses of the user are stored in the HashSet activeKeys
//this makes dealing with inputs a lot easier, no duplicates, not messy
public class Inputs implements KeyListener {

	private static LinkedHashSet<Integer> activeKeys;
	
	public Inputs() {
		activeKeys = new LinkedHashSet<Integer>();
	}
	
	@Override 
	public void keyPressed(KeyEvent e) {		
		activeKeys.add(e.getKeyCode());				
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		activeKeys.remove(e.getKeyCode());
	}
	
	@Override
	public void keyTyped (KeyEvent e) {
	}
	
	public static LinkedHashSet<Integer> getActiveKeys() {
		return activeKeys;
	}
	
	
}
