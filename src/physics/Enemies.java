package physics;

import java.util.ArrayList;

public class Enemies {
	public ArrayList<Enemy> enemies;
	public int level;
	public Enemies(int level) {
		this.level = level;
	}
	
	public void loadEnemies() {
		enemies = new ArrayList<Enemy>();
		switch (level) {
			case 1:
				
			case 2:
				
			case 3:
				
			case 4:
				
			case 5:
				
			case 6:
				
			case 7:
				
			case 8:
				
			case 9:
				
			default:
				break;
		}
	}
	
	public void increaseLevel() {
		level++;
	}
	
	public void resetLevel() {
		level = 1;
	}
}
