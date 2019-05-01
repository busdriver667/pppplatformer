package physics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy {
	private int x;
	private int y;
	private Rectangle hitbox;
	private static int VELOCITY;
	private BufferedImage image;
	private static int WIDTH;
	private static int HEIGHT;
	private String name;
	
	public Enemy(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		loadInfo();
	}
	
	protected void init() {
		
	}
	
	protected void loadInfo() {
		try{
			image = ImageIO.read(getClass().getResource("/drawables/elements/" + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void move() {
		this.x += VELOCITY;
	}
	
}
