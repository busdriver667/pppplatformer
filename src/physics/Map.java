package physics;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class Map {

	public static BufferedImage BG;
	public static Tile[][] tileMap;
	public static Tile[][] displayedMap;
	public static int ROWS = 11;
	public static int COLUMNS = 21; //default number of columns
	
	public Map() {
	}
	
	public void initLevel(int level) {
		//get level background based on name
		try {
			BG = ImageIO.read(getClass().getResource("/drawables/bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//get level 
		//the level map is a txt file m * n words, containing blocktypes
		//"blank"/passable tiles are null
		InputStream is = this.getClass().getResourceAsStream("/levels/" + level + ".txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String row = null;
		String []tileArray = new String[ROWS];
		displayedMap = new Tile[ROWS][COLUMNS];
		try {
			int i = 0;
			while((row = reader.readLine()) != null) {
				tileArray = row.split("\\s");
				
				if (i == 0){
					COLUMNS = tileArray.length;
					tileMap = new Tile[ROWS][COLUMNS];
				}
				
				for (int j = 0; j < COLUMNS - 1; j ++) {
					if (tileArray[j].equals("blank"))
						tileMap[i][j] = null;						
					else 
						tileMap[i][j] = spawnBlock(tileArray[j], i, j);										
				} i++;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < displayedMap[1].length ; j++) {
				displayedMap[i][j] = tileMap[i][j];
			}
		}
	}
	//switch statement to spawn block based on type
	private Tile spawnBlock(String name, int i, int j){
		switch (name) {
			case "terra":
				return new Block("terra", i, j);
			case "terrL":
				return new Block("terrL", i, j);
			case "terrR":
				return new Block("terrR", i, j);
			case "terrE":
				return new Block("terrE", i, j);
			case "wallL":
				return new Block("wallL", i, j);
			case "wallR":
				return new Block("wallR", i, j);
			case "darkM":
				return new Block("darkM", i, j);
			default:
				break;
		}
		return null;
	}
	
	public static void updateMap(int offset) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = offset; j < displayedMap[1].length + offset; j++) {				
					displayedMap[i][j - offset] = tileMap[i][j];				
			}
		}
	}
}
