package com.game.src.main;

import java.awt.Graphics; 
import java.awt.Rectangle;
import java.util.Random;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;
import com.game.src.main.libs.Animation;

public class Enemy extends GameObject implements EntityB  {


	
	private Textures tex; //there is just one initialised textures object and always we are using it
	
	Animation anim;
	Random r = new Random();
	private Game game;
	private Controller c;// we need controller class to remove entity
	
	private int speed = r.nextInt(3)+1;
	
	public Enemy(double x, double y, Textures tex, Controller c, Game game){
		super(x, y);
		this.tex = tex;
		this.game = game;
		this.c = c;
		
		anim = new Animation(5, tex.enemy[0], tex.enemy[1], tex.enemy[2]);
	}
	
	public void tick(){// if the object moves at all you need tick updating method
		y += speed;//changing the speed of enemy randomly by changing y 	
			
		if(y > (Game.HEIGHT * Game.SCALE)){//taking enemy back up when disappears from button
			speed = r.nextInt(3)+1;//changing the enemy's speed at each loop
			y = -10;
			x = r.nextInt(Game.WIDTH * Game.SCALE);//changing the enemy's start position on each loop
		}
		
		for (int i = 0; i < game.ea.size(); i++) {
			EntityA tempEnt = game.ea.get(i);
			if(Physics.Collision(this, tempEnt)){
				c.removeEntity(tempEnt);//removes the bullet
				c.removeEntity(this);//removes the enemy 
				game.setEnemy_killed(game.getEnemy_killed()+1);
			}
		}
		
		anim.runAnimation();
		
	}
	public void render(Graphics g){
		anim.drawAnimation(g, x, y, 0);
	}

	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 32, 32);
	}

	public double getX() {
		return x;
	}

	
	public double getY() {
		return y;
	}
	
	
}
