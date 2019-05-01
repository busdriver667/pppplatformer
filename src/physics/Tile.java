package physics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Tile {
	
	protected int row;
	protected int column;
	
	BufferedImage image;
	protected Rectangle hitbox;
	public static final int SIZE = 64;
	protected int x;
	protected int y;
	
	public Tile(int i, int j) {
		this.row = i;
		this.column = j;
	}
	
	protected abstract void init();
	
	protected abstract void loadImage();
	
	public BufferedImage getImage(){
		return image;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void updateX(int offset) {
		this.x = this.column * SIZE + offset;
	}
	
	public void updateHitbox() {		
		this.hitbox = new Rectangle(this.x, this.y, SIZE, SIZE);
	}
}
