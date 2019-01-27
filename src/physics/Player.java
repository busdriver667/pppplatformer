package physics;

import gui.Window;
import gui.PlayArea;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
	//player position and height
	public static final int PLAYER_START_X = 120;
	private final int PLAYER_HEIGHT = 64;
	private final int PLAYER_WIDTH = 40;
	private int x = PLAYER_START_X;
	private int y = Window.HEIGHT - PlayArea.TERRAIN_HEIGHT - PLAYER_HEIGHT;
	
	private boolean jumping = false; //state of character jump
	private int jumpCount = 0; //how many frames character has been jumping for
	private static final int MAX_JUMP_COUNTER = 15;
	private boolean idle = true;
	
	private static final int VELOCITY = 5; //how many pixels the player moves in a single frame
	private int moveCounter = 0; //counting frames of animation 
	private static final int MAX_MOVE_COUNTER = 5; //max possible value of counter
	private static final int BUFFER_RUN_SIZE = 6; //number of frames in run animation
	private int currentFrameNumber = 0; //current frame of run animation
	
	//bufferedImages for animation
	private BufferedImage currentFrame; //current frame of animation 
	private BufferedImage idle_R; //idle facing right
	private BufferedImage idle_L; //idle facing left
	private BufferedImage[] run_R;
	private BufferedImage[] run_L;
	private Rectangle hitBox;
	private int last_direction = KeyEvent.VK_RIGHT; //initially facing right
	
	public Player() {
		hitBox = new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		//arrays for run animation frames
		run_L = new BufferedImage[BUFFER_RUN_SIZE];
		run_R = new BufferedImage[BUFFER_RUN_SIZE];
		
		//load our image info
		loadInfo();
		
		//starting frame is idle facing right
		currentFrame = idle_R;		
	}
	
	private void loadInfo() { //load frames to their respective buffers
		try {
			idle_R = ImageIO.read(getClass().getResource("/drawables/idle_R.png"));
			idle_L = ImageIO.read(getClass().getResource("/drawables/idle_L.png"));
			
			for (int i = 0 ; i < BUFFER_RUN_SIZE; i++) {
				run_R[i] = ImageIO.read(getClass().getResource("/drawables/run_R" + i + ".png"));
				run_L[i] = ImageIO.read(getClass().getResource("/drawables/run_L" + i + ".png"));
			}
;		} catch (IOException e) {
			System.out.println("test");
			e.printStackTrace();
		}
	}
	
	public void move(int direction){
		//case statement to decide which way to move the player
		switch (direction) {
		case KeyEvent.VK_LEFT:
			idle = false;
			//move left and set hitbox
			x = x - VELOCITY;
			hitBox.setLocation(x, y);
			
			//update frame information
			setFrameNumber();
			currentFrame = run_L[currentFrameNumber];
			last_direction=KeyEvent.VK_LEFT;
			break;
			
		case KeyEvent.VK_RIGHT:
			idle = false;
			//move right and set hitbox
			x = x + VELOCITY;
			hitBox.setLocation(x, y);
			
			//update frame information
			setFrameNumber();
			currentFrame = run_R[currentFrameNumber];
			last_direction=KeyEvent.VK_RIGHT;
			break;
			
		default:
			break;
		}
		moveCounter++;
	}
	
	//helper function for jumping. initiates values for a jump.
	public void jump(){
		this.jumping = true;
		
		this.jumpCount = 0;
		
		if(last_direction == KeyEvent.VK_RIGHT) {
			currentFrame = run_R[2];
		} else {
			currentFrame = run_L[2];
		}
	}
	
	//this function makes the player jump, if the status of the player is jumping, 
	//implement the current step in the jump counter
	public void checkJumpState() {
		if (jumping) 
			if(jumpCount < MAX_JUMP_COUNTER) { //if player is not at max jump height
				if(jumpCount < 1) {
					if(last_direction == KeyEvent.VK_RIGHT)
						x += VELOCITY ;
					else
						x -= VELOCITY ;
				}
				//make player decelerate as he reaches apex
				y -= VELOCITY + (MAX_JUMP_COUNTER - jumpCount);
				hitBox.setLocation(x,  y);
			} else {
				//accelerate towards the ground, the -1 on each iteration to balance equation
				y += VELOCITY - (MAX_JUMP_COUNTER - jumpCount - 1);
				hitBox.setLocation(x,  y);
			}
			
		jumpCount++;
		
		if (jumpCount >= MAX_JUMP_COUNTER * 2) {
			jumping = false;
			jumpCount = 0;
		}
		
	}
	//once the player stops, he should face the direciton he was moving
	public void stop() {
		moveCounter = 0;
		idle = true;
		if (last_direction == KeyEvent.VK_RIGHT) 
			currentFrame = idle_R;
		else
			currentFrame = idle_L;
	}
	
	//may need more specific functions when we have more animations, for now this sets the current frame the player is in
	//moveCounter is updated in gameThread, and allows us to correctly identify the current frame for animation
	private void setFrameNumber() {
		currentFrameNumber = moveCounter/MAX_MOVE_COUNTER;
		currentFrameNumber %= BUFFER_RUN_SIZE;
		
		if (moveCounter > MAX_MOVE_COUNTER * 6) moveCounter = 0;
	}
	
	public BufferedImage getCurrentFrame() {
		return currentFrame;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getJumping() {
		return jumping;
	}
}
