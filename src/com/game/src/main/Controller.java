package com.game.src.main;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

// This class control all our entities
public class Controller {

	//to keep all current entities (bullet, enemy...), anything   we want more than 1 we need to put them in a list 
	private LinkedList<EntityA> ea = new LinkedList<EntityA>();
	private LinkedList<EntityB> eb = new LinkedList<EntityB>();
	
	//certain entity variables (bullet, enemy...)
	private EntityA enta;
	private EntityB entb;
	
	private Textures tex;
	private Game game;
	
	Random r = new Random();
	
	public Controller(Textures tex, Game game){
		this.tex = tex;
		this.game = game; 
		
	}
	public void createEnemy(int enemy_count){
		for(int i = 0; i < enemy_count; i++)
			addEntity(new Enemy(r.nextInt(640), 10, tex, this, game));
	}
	
	//ticking all entities
	public void tick(){
		//A class, for loop takes all EntityA objects from the list and updates them through their tick() methods
		for(int i = 0; i < ea.size(); i++){
			enta = ea.get(i);
			
			//removing bullet when its out of screen 
			if(enta.getY() < 0){
				removeEntity(enta);
			}
			
			enta.tick();
		}
		//B class
		for(int i = 0; i < eb.size(); i++){
			entb = eb.get(i);
			
			entb.tick();
		}
	}
	
	//rendering all entities
	public void render(Graphics g){
		//A class, for loop takes all EntityA objects from the list and renders them
		for(int i = 0; i < ea.size(); i++){
			enta = ea.get(i);
			
			enta.render(g);
		}
		//B class
		for(int i = 0; i < eb.size(); i++){
			entb = eb.get(i);
			
			entb.render(g);
		}
	}
	
	public void addEntity(EntityA block){
		ea.add(block);
	}
	public void removeEntity(EntityA block){
		ea.remove(block);
	}
	
	public void addEntity(EntityB block){
		eb.add(block);
	}
	public void removeEntity(EntityB block){
		eb.remove(block);
	}
	
	public LinkedList<EntityA> getEntityA(){
		return ea;
	}
	
	public LinkedList<EntityB> getEntityB(){
		return eb;
	}
		
		
	
}
