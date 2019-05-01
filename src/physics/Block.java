package physics;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Block extends Tile {
		
	private String name;
	public Block(String name, int i, int j) {
		super(i, j);
		this.name = name;
		loadImage();
		init();
	}
	
	@Override
	protected void init() {
		this.x = column * SIZE;
		this.y = row * SIZE;
		hitbox = new Rectangle(x, y, SIZE, SIZE);
	}
	
	//load the image for this block from our files
	protected void loadImage() {
		try{
			image = ImageIO.read(getClass().getResource("/drawables/elements/" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
