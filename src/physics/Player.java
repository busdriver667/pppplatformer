package physics;

import gui.Window;
import gui.PlayArea;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
	// player position and height values
	public static final int XOFFSET = 16; // offset of jpanel in jframe
	public static final int YOFFSET = 24;
	private static final int PLAYER_HEIGHT = 40;
	private static final int PLAYER_WIDTH = 30;
	public static final int PLAYER_START_X = 64;
	public static final int PLAYER_START_Y = Window.HEIGHT - (PlayArea.TILESIZE * 3)
			+ (PlayArea.TILESIZE - PLAYER_HEIGHT);
	private int x = PLAYER_START_X;
	private int y = PLAYER_START_Y;
	private int playerRow = (x) / PlayArea.TILESIZE; // row and column the
														// player is currently
														// in
	private int playerCol = (y) / PlayArea.TILESIZE;

	// helper variables
	private boolean jumping = false;
	private boolean falling = false;
	private boolean hurt = false;
	private int jumpCounter = 0; // how many frames character has been jumping

	private static final int MAX_JUMP_COUNTER = 30; // how many frames you jump
													// for
	private int fallCounter = 0;
	private boolean idle = true;
	public static int lives = 3;
	private int hp = lives;
	private int jumpTotal = 0;
	private boolean doubleJumping = false;
	private int doubleJumpCount = MAX_JUMP_COUNTER;
	public static final int VELOCITY = 5; // how many pixels the player moves
											// in a single frame
	private int runCounter = 0; // counting frames of animation (sort of)
	private static final int BUFFER_RUN_SIZE = 4; // number of frames in run
													// animation
	private int runFrameNumber = 0; // current frame of run animation

	// bufferedImages for animation
	private BufferedImage currentFrame; // current frame of animation
	private BufferedImage idle_R; // idle facing right
	private BufferedImage idle_L; // idle facing left
	private BufferedImage[] run_R;
	private BufferedImage[] run_L;
	private BufferedImage[] walk_R;
	private BufferedImage[] walk_L;
	private BufferedImage[] jump_R;
	private BufferedImage[] jump_L;
	private Rectangle hitbox;
	private int facing = KeyEvent.VK_RIGHT; // initially facing right

	public Player() {
		hitbox = new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		// PlayArea.addPlayer(this);
		// arrays for run animation frames
		run_L = new BufferedImage[BUFFER_RUN_SIZE];
		run_R = new BufferedImage[BUFFER_RUN_SIZE];
		walk_L = new BufferedImage[BUFFER_RUN_SIZE];
		walk_R = new BufferedImage[BUFFER_RUN_SIZE];
		jump_L = new BufferedImage[BUFFER_RUN_SIZE];
		jump_R = new BufferedImage[BUFFER_RUN_SIZE];
		// load our image info
		loadInfo();

		// starting frame is idle facing right
		currentFrame = idle_R;
	}

	private void loadInfo() { // load frames to their respective buffers
		try {
			idle_R = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/stand_R.png"));
			idle_L = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/stand_L.png"));

			for (int i = 0; i < BUFFER_RUN_SIZE; i++) {
				run_R[i] = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/run_R" + i + ".png"));
				run_L[i] = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/run_L" + i + ".png"));
				walk_R[i] = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/walk_R" + i + ".png"));
				walk_L[i] = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/walk_L" + i + ".png"));
				jump_R[i] = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/jump_R" + i + ".png"));
				jump_L[i] = ImageIO.read(getClass().getResource("/drawables/buddhaPlayer/jump_L" + i + ".png"));
			}
			;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		checkFallState();
		implementJump();
		//System.out.println(jumping);	
	}

	public void move(int direction) {
		// case statement to decide which way to move the player
		// move right/left, update hitbox, check collisions, update frame
		// update facing direction
		switch (direction) {
		case KeyEvent.VK_LEFT:
			idle = false;
			if (jumping) {
				setJumpFrameNumber();
			} else {
				setFrameNumber();
				currentFrame = run_L[runFrameNumber];
			}
			facing = KeyEvent.VK_LEFT;
			if (checkXCollisions() || checkEdgeCollision())
				break;
			x -= VELOCITY;
			hitbox.setLocation(x, y);
			break;
		case KeyEvent.VK_RIGHT:
			idle = false;

			if (jumping) {
				setJumpFrameNumber();
			} else {
				setFrameNumber();
				currentFrame = run_R[runFrameNumber];
			}
			facing = KeyEvent.VK_RIGHT;
			if (checkXCollisions())
				break;
			x += VELOCITY;
			hitbox.setLocation(x, y);
			break;
		default:
			break;
		}
		runCounter++;
	}

	// initiate jump
	public void setJumping() {
		if (jumping) {
			doubleJumping = true;
			jumping = false;
			jumpCounter = 0;
		} else if (doubleJumping) {
			jumping = false;
			return;
		}
		jumping = true;
		doubleJumping = false;
		jumpCounter = 0;
	}

	public void setFalling() {
		if (!jumping && checkFalling()) {
			falling = true;
			if (facing == KeyEvent.VK_RIGHT) {
				currentFrame = jump_R[jump_R.length - 1];
			} else {
				currentFrame = jump_L[jump_L.length - 1];
			}
		} else {
			falling = false;
		}
	}
	
	public boolean checkFalling() {
		if (playerRow + 1 >= Map.ROWS) {
			return true;
		}
		int middleX = (int) Math.round(hitbox.getCenterX());
		int leftX = (int) Math.round(hitbox.getMinX() - PLAYER_WIDTH + 1);
		int rightX = (int) Math.round(hitbox.getMaxX() + PLAYER_WIDTH - 1);

		// tiles to the right and left of where the player is standing
		int currentColumn = Math.round((middleX) / PlayArea.TILESIZE);
		int leftColumn = Math.round(leftX / PlayArea.TILESIZE);
		int rightColumn = Math.round(rightX / PlayArea.TILESIZE);

		Tile leftTile = Map.displayedMap[playerRow + 1][leftColumn];
		Tile rightTile = Map.displayedMap[playerRow + 1][rightColumn];

		boolean leftTileBlank = !(leftTile instanceof Block);
		boolean rightTileBlank = !(rightTile instanceof Block);

		// if player is not currently standing on a block, player is falling
		if (!(Map.displayedMap[playerRow + 1][currentColumn] instanceof Block)) {

			if (rightTileBlank && !leftTileBlank) {
				if (hitbox.getCenterX() >= leftTile.getX() + PlayArea.TILESIZE) {
					return true;
				}
			} else if (leftTileBlank && !rightTileBlank) {
				if (hitbox.getCenterX() <= rightTile.getX()) {
					return true;
				}
			} else if (rightTileBlank && leftTileBlank) {
				return true;
			}
		}
		return false;
	}

	// player dies, reset values and decrease a life
	// DONE
	private void die() {
		x = PLAYER_START_X;
		y = PLAYER_START_Y;
		playerRow = y / PlayArea.TILESIZE;
		playerCol = x / PlayArea.TILESIZE;
		hitbox.setLocation(x, y);
		facing = KeyEvent.VK_RIGHT;
		falling = false;
		lives--;
		Map.updateX(0);
	}

	// get the block on the row above the player and check intersection
	// DONE
	private boolean checkJumpCollisions() {
		playerRow = (int) (hitbox.getCenterY() / PlayArea.TILESIZE);
		playerCol = (int) (hitbox.getCenterX() / PlayArea.TILESIZE);
		Tile above = Map.displayedMap[playerRow][playerCol];
		if (above instanceof Block) {
			if (hitbox.intersects(above.getHitbox())) {
				return true;
			}
		}
		return false;
	}

	
	//check X collisions for player and blocks
	private boolean checkXCollisions() {
		if (playerCol < 1)
			return false;
		if (jumping) {
			playerRow = (int) (hitbox.getMaxY() / PlayArea.TILESIZE);
		} else {
			playerRow = (int) (hitbox.getCenterY() / PlayArea.TILESIZE);
		}
		
		if (facing == KeyEvent.VK_LEFT) {
			playerCol = (int) (hitbox.getMinX() / PlayArea.TILESIZE);
		} else
			playerCol = (int) (hitbox.getCenterX() / PlayArea.TILESIZE);
		
		Tile adjacent = null;
		
		if (facing == KeyEvent.VK_LEFT && playerCol > 0)
			adjacent = Map.displayedMap[playerRow][playerCol - 1];
		else if (facing == KeyEvent.VK_RIGHT)
			adjacent = Map.displayedMap[playerRow][playerCol + 1];
		if (adjacent instanceof Block) {
			if (hitbox.intersects(adjacent.getHitbox())) {
				return true;
			}
		}
		
		
		playerRow = (int) (hitbox.getMaxY() / PlayArea.TILESIZE);
		playerCol = (int) (hitbox.getCenterX() / PlayArea.TILESIZE);
		Tile adjacentLeft = null;
		Tile belowLeft = null;
		Tile adjacentRight = null;
		Tile belowRight = null;
		Tile Below = Map.displayedMap[playerRow][playerCol];
		adjacentLeft = Map.displayedMap[playerRow][playerCol - 1];
		belowLeft = Map.displayedMap[playerRow - 1][playerCol - 1];
	
		adjacentRight = Map.displayedMap[playerRow][playerCol + 1];
		belowRight = Map.displayedMap[playerRow - 1][playerCol + 1];
		
		if (adjacentLeft instanceof Block)
			if(hitbox.intersects(adjacentLeft.getHitbox()))
				return true;
		if (belowLeft instanceof Block)
			if(hitbox.intersects(belowLeft.getHitbox()))
				return true;
		if (adjacentRight instanceof Block)
			if(hitbox.intersects(adjacentRight.getHitbox()))
				return true;
		if (belowRight instanceof Block)
			if(hitbox.intersects(belowRight.getHitbox()))
				return true;
		if (Below instanceof Block) {
			if (hitbox.intersects(Below.getHitbox())) {
				return true;
			}
			
			return false;
		}
		return false;
	}
	

	// this function implements jump/doublejump by keeping track of the max
	// number of frames the player can jump for
	private void implementJump() {
		if (!checkJumpCollisions()) {
			jumpCounter++;
			if (jumpCounter >= MAX_JUMP_COUNTER) {
				doubleJumping = false;
				jumping = false;	
				falling = true;					
			}
			if (doubleJumping) {
				if (jumpCounter < MAX_JUMP_COUNTER) {
					y -= VELOCITY;
					hitbox.setLocation(x, y);
				}							
			} else if (jumping) {
				if (jumpCounter < MAX_JUMP_COUNTER) {// if player is not at max
					y -= VELOCITY;
					hitbox.setLocation(x, y);
				}				
			}			
		} else {
			jumping = false;
			doubleJumping = false;
			falling = true;
		}
		if (!checkFalling()) {
			jumpCounter = 0;
			jumpTotal = 0;
		}
	}
	
	//implements our player falling
	private void checkFallState() {
		
		playerRow = y / PlayArea.TILESIZE;

		if (playerRow + 1 >= Map.ROWS) {
			die();
			return;
		}

		if (falling) {
			y += VELOCITY;
			playerRow = y / PlayArea.TILESIZE;
			hitbox.setLocation(x, y);
			fallCounter++;
			int animFrame = jumpCounter < 15 ? 2 : 3;
			if (facing == KeyEvent.VK_RIGHT) {
				currentFrame = jump_R[animFrame];
			} else {
				currentFrame = jump_L[animFrame];
			}
		}
		setFalling();
	}
	public boolean checkEdgeCollision() {
		if (playerCol == 0) {
			if (x < 5)
				return true;
		}
		return false;
	}
	// once the player stops, he should face the direciton he was moving
	public void stop() {
		runCounter = 0;
		jumpCounter = 0;
		idle = true;
		if (facing == KeyEvent.VK_RIGHT)
			currentFrame = idle_R;
		else
			currentFrame = idle_L;
	}

	// frame number for run animation, called from move()
	private void setFrameNumber() {
		runFrameNumber = (runCounter / BUFFER_RUN_SIZE) % BUFFER_RUN_SIZE;
		
		// allow move counter to get quite large so that the animation is spread
		// over a larger number of frames
		if (runCounter > BUFFER_RUN_SIZE * 10)
			runCounter = 0;
	}

	private void setJumpFrameNumber() {
		int frameNo = (jumpCounter / (MAX_JUMP_COUNTER * 30)) % (jump_R.length - 1);
		if (facing == KeyEvent.VK_RIGHT) {
			currentFrame = jump_R[frameNo];
		} else {
			currentFrame = jump_L[frameNo];
		}
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

	public Rectangle getHitbox() {
		return hitbox;
	}

	public boolean getJumping() {
		return jumping;
	}

	public boolean getDoubleJumping() {
		return doubleJumping;
	}

	public boolean getFalling() {
		return falling;
	}
}
