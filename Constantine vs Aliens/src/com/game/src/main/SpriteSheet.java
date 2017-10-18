package com.game.src.main;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage image;
	
	//assign the sprite sheet to image 
	public SpriteSheet(BufferedImage image){
		this.image = image;
	}
	  
	//return subimages from sprite sheet
	public BufferedImage grabImage(int col, int row, int width, int height){
		BufferedImage img = image.getSubimage((col * 32) - 32, (row * 32) - 32, width, height);
		return img;
	}
}
