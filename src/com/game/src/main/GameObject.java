package com.game.src.main;

import java.awt.Rectangle;

public class GameObject {

	public double x, y;
	public GameObject(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	//we using rectangle because it has .intersects method and it helps us to detect collision.
	public Rectangle getBounds(int width, int height){
		return new Rectangle((int)x, (int)y, width, height);
	}
}
