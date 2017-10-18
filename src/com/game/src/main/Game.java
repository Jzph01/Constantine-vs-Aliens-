package com.game.src.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;
// we adding res folder to the build path to access sprite sheet
// Canvas is rectangle area that we can put anything we want in 
public class Game extends Canvas implements Runnable{ 

	//frame dimensions for our window
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "2D Space Game";
	
	private boolean running = false;
	private Thread thread;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);//buffers whole window and we're going to be access RedGreenBlue
	private BufferedImage spriteSheet = null;
	private BufferedImage background = null;
	
	private boolean is_shooting = false;//making bullet one at press and release 
	
	private int enemy_count = 5;
    private int enemy_killed = 0;
	
	
	private Player p;
	private Controller c;
	private Textures tex;
	private Menu menu;
	
	public LinkedList<EntityA> ea;
	public LinkedList<EntityB> eb;
	
	public static int HEALTH = 100 * 2;
	
	public static  enum STATE{
		MENU,
		GAME
	};
	
	public static STATE State = STATE.MENU;//for checking which state we are in 
	
	public void init(){
		requestFocus();//with this method you don't need to press the screen to activate movements  
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try{
			spriteSheet = loader.loadImage("/sprite-Sheet.png");
			background = loader.loadImage("/Backgroun.png");
		}catch(Exception e){ 
			e.printStackTrace();
		}
		
		addKeyListener(new KeyInput(this));
		
		this.addMouseListener(new MouseInput());
		
		tex = new Textures(this);//tex must be before player and controller because they use its textures
	
		
		c = new Controller(tex, this); 
		p = new Player(200, 200, tex, this, c);//"this"->for game in player constructor
		
		c.createEnemy(enemy_count);
		
		menu = new Menu();//initializing menu
		
		ea = c.getEntityA();//getting a list of entity A
		eb = c.getEntityB();
	}
	
	//starting the thread
	private synchronized void start(){
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
		
	}
	
	//stops the thread
	private synchronized void stop(){//Synchronised dealing with threads
		if(!running)
			return;
		
		running = false;
		try {
			thread.join();//joins all the threads together and waits them to die
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	// Runnable interface need that run() method / what Runnable does is when the thread is called or start it will take that
	//thread and call run() method, and this is where our game loop going to be.
	public void run(){
		init();
		
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;//we want updating 60 times per second// 60fps
		double ns = 1000000000 / amountOfTicks;// nanoseconds per tick, billion nanoseconds are 1 second.
		double delta = 0;// unprocessed amount of time that we haven't processed yet, which will help us determine when we need to tick
		int updates = 0; 
		int frames = 0;
		boolean canRender = false;//we only want to render when it is updated.
		long timer = System.currentTimeMillis();
		
		//This would be the game loop running is true when thread starts, we're going to have the game loop to update the 
		//game at certain amount of time 
		while(running){ 
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;//delta is now shows how many times we need to tick(update), (now - lastTime) is unprocessed time
			lastTime = now;
			if(delta >= 1){
				tick();
				updates++;//shows how many times we tick
				delta--;
				canRender = true;
			}//else{
				//canRender = false;
			//}
			
			if(canRender){
				render();
				frames++; 				
			}
			
			
			//calculating how much fps and ticks is gone by  
			//1000 milliseconds is 1 second, 
			if(System.currentTimeMillis() - timer > 1000){//if min 1 second is pass, then ...
				timer += 1000;
				System.out.println(updates+" Ticks, Fps "+frames);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();	
	}
	
	private void tick(){//everything in the game updates#
		if(State == STATE.GAME){//only run through these tick methods if we have proper state
			p.tick();
			c.tick();
		}
		 if(enemy_killed >= enemy_count){
	            enemy_count += 2;
	            enemy_killed = 0; 
	            c.createEnemy(enemy_count);
	        }

	}
	
	private void render(){//everything in the game renders
		BufferStrategy bs = this.getBufferStrategy();//handles all the buffer behind the scenes, "this" accessing our Canvas class 
		if(bs == null){
			createBufferStrategy(3);//3 means behind the current buffer, 2 more buffers loading
			return;
		}
		
		//create graphics context for drawing buffers // so takes the ready buffer and shows at the screen.
		Graphics g = bs.getDrawGraphics();
		/////////////////////////////////////////////////////////////
		//drawing out stuff
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);//"this"->image observer, getWith() -> screens with and height
		g.fillRect(0, 0, 800, 800); 
		
		g.drawImage(background, 0, 0, null);//background for game and for the menu (for all states)
		
		if(State == STATE.GAME){
			//g.drawImage(background, 0, 0, null);->you can put new background for game
			p.render(g);
			c.render(g);
			
			//health bar 
			g.setColor(Color.gray);
			g.fillRect(5, 5, 200, 50);//place and the size of health bar
			
			g.setColor(Color.green);
			g.fillRect(5, 5, HEALTH, 50);//change the width with health
			
			g.setColor(Color.white);
			g.drawRect(5, 5, 200, 50);
			
		}else if(State == STATE.MENU){//we can put background for menu in here
			menu.render(g);
		}
		
		
		///////////////////////////////////////////////////////////
		g.dispose();//elden cikarmak(dispose current buffer)
		bs.show();//shows our bufferStrategy(shows the buffer has been calculated) and will make the next available buffer visible
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(State == STATE.GAME){
			if(key == KeyEvent.VK_RIGHT){//VK=Virtual keyboard
				p.setVelX(5);
			}else if(key == KeyEvent.VK_LEFT){
				p.setVelX(-5);
			}else if(key == KeyEvent.VK_DOWN){
				p.setVelY(5);
			}else if(key == KeyEvent.VK_UP){
				p.setVelY(-5);
			}else if(key == KeyEvent.VK_SPACE && !is_shooting){
				is_shooting = true;// in order to shoot another bullet player must release the key
				c.addEntity(new Bullet(p.getX(), p.getY(), tex, this));
			}
		}
		
	}
	public void keyReleased(KeyEvent e){// when we release the key setting the velocity back to zero  
int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_RIGHT){//VK=Virtual keyboard
			p.setVelX(0);
		}else if(key == KeyEvent.VK_LEFT){
			p.setVelX(0);
		}else if(key == KeyEvent.VK_DOWN){
			p.setVelY(0);
		}else if(key == KeyEvent.VK_UP){
			p.setVelY(0);
		}else if(key == KeyEvent.VK_SPACE){
			is_shooting = false;
		}
		
	}
	
	public static void main(String[] args){
		Game game = new Game();
		
		//our dimensions
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);//taking our dimensions
		frame.pack();//?????what it does
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	
		game.start();
	}
	
	public BufferedImage getSpriteSheet(){
		return spriteSheet;
	}
	
	public int getEnemy_count(){
        return enemy_count;
    }
    public void setEnemy_count(int enemy_count){
        this.enemy_count = enemy_count;
    }
    public int getEnemy_killed(){
        return enemy_killed;
    }
    public void setEnemy_killed(int enemy_killed){
        this.enemy_killed = enemy_killed;
    }
    public  Controller getC(){
        return  c;
    }


}
