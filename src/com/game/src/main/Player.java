																																																														package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;
import com.game.src.main.libs.Animation;

public class Player extends GameObject implements EntityA{

	
	//###############################################
	private double velX = 0;//velocity -> hiz
	private double velY = 0;//using these for faster respond for keyPress
	//#######################################################
	
	private Textures tex;
	private Game game;
	private Controller controller;
	Animation anim;
	
	public Player(double x, double y, Textures tex, Game game, Controller controller){
		super(x, y);
		this.tex = tex;
		this.game = game;
		this.controller = controller;
		 
		anim = new Animation(5, tex.player[0], tex.player[1], tex.player[2]);
	}
	
	public void tick(){//update method
		x += velX;
		y += velY;
		
		//keeping player just in the frame
		if(x <= 0)
			x = 0;
		if(x >= 640 - 18)
			x = 640 - 18;
		if(y <= 0)
			y = 0;
		if(y >= 480 - 32)
			y = 480 - 32;
		
		//checks each enemy for collision with player 
		for(int i = 0; i < game.eb.size(); i++){
			
			EntityB tempEnt = game.eb.get(i);
			
			if(Physics.Collision(this, tempEnt)){
				controller.removeEntity(tempEnt);
				Game.HEALTH -= 10;
				game.setEnemy_killed(game.getEnemy_killed()+1);
			}
		}
		
		anim.runAnimation();
	}
	
	 public void render(Graphics g){//drawing out our image
		 anim.drawAnimation( g, x, y, 0 );
		 
	 }
	 
	 public Rectangle getBounds(){
			return new Rectangle((int)x, (int)y, 32, 32);
		}
	 
	 public double getX(){
		 return x;
	 }
	 public double getY(){
		 return y;
	 }
	 public void setX(double x){
		 this.x = x;
	 }
	 public void setY(double y){
		 this.y = y;
	 }
	 public void setVelX(double velX){
		 this.velX = velX;
	 }
	 public void setVelY(double velY){
		 this.velY = velY;
	 }
}
