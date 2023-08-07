package main;


import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;

import entities.Bullet;
import entities.Enemy;
import entities.Mario;
import entities.MeleeEnemy;
import entities.Player;
import entities.TimeEntity;
import worldGeometry.Booster;
import worldGeometry.Platform;
import processing.core.PApplet;
import processing.core.PImage;

public class DrawingSurface extends PApplet {

	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 600;

	public static final String fileSeparator = System.getProperty("file.separator");

	private PImage backgrounds;
	private Rectangle screenRect;
	private ArrayList<Booster> boosters;
	private int tSinceLast;
	private int lvl;
	private int completed;
	private Player player;
	private ArrayList<Shape> obstacles;
	private ArrayList<Platform> platforms;
	private ArrayList<Integer> keys;
	private ArrayList<MeleeEnemy> meleeEnemies; 
	private ArrayList<Bullet> bullets; 
	private Shape goal;
	private boolean jumpRelease;
	private boolean skillRelease;
	private boolean attackRelease;
	private ArrayList<PImage> assets;
	private long total = 0;
	private int frameCount = 0;
	private int averageFPS = 0;
	private boolean first = true;
	private double timeSpeed = 1;
	private long start = System.nanoTime();
	
	
	
	
	public DrawingSurface() {


		super();
		assets = new ArrayList<PImage>();
		keys = new ArrayList<Integer>();

		screenRect = new Rectangle(0,0,DRAWING_WIDTH,DRAWING_HEIGHT);
		boosters = new ArrayList<Booster>();
		obstacles = new ArrayList<Shape>();
		platforms = new ArrayList<Platform>();
		meleeEnemies = new ArrayList<MeleeEnemy>(); 
		bullets = new ArrayList<Bullet>();
		TimeEntity.setTimeRate(timeSpeed);
		for(Shape s : obstacles) {
			platforms.add(new Platform(s));
		}




	}

	public void initLevel(String fileName) {
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader bReader = new BufferedReader(reader);
			char[] chars = null;
			completed++;
			double yoff = 0;
			double xoff = 0;
			int rows = 24;
			double pHeight = DRAWING_HEIGHT/rows;
			while(bReader.ready()) {
				chars = bReader.readLine().toCharArray();
				for(char c : chars) {
					if(c == '#') {
						obstacles.add(new Rectangle((int)xoff,(int)yoff,(int)pHeight,(int)pHeight));

					}
					else if( c == 'b') {
						boosters.add(new Booster(xoff,yoff,(int)pHeight,(int)pHeight));
					}
					else if(c == 'm') {
						meleeEnemies.add(new MeleeEnemy(assets.get(1),(int)xoff,(int)yoff,5*completed,5+5*completed, this));
					}
					else if(c == 'g') {
						goal = new Rectangle((int)xoff,(int)yoff,(int)pHeight,(int)pHeight);
					}
					else if(c=='M')
					{
						meleeEnemies.add(new MeleeEnemy(assets.get(1),(int)xoff,(int)yoff,40*completed,10+5*completed, this));
					}
					else {

					}
					xoff+=pHeight/2;
				}
				yoff+= pHeight;
				xoff = 0;


			}


		} 
		catch (IOException e) {
			e.printStackTrace();
		} 

	}
	public void spawnNewPlayer() {
		player = new Player(assets.get(0), DRAWING_WIDTH/2-Mario.MARIO_WIDTH/2,50,this);
		for(int i = 0; i < 0; i++) {
			player.levelUP();
		}
	}

	public void respawnPlayer() {
		player.resetHP();
		player.resetEP();
		player.moveToLocation( DRAWING_WIDTH/2-Mario.MARIO_WIDTH/2,50);
		player.accelerate(-player.getDx(), -player.getDy());
	}



	public void runMe() {
		runSketch();
	}

	// The statements in the setup() function 
	// execute once when the program begins
	public void setup() {
		//size(0,0,PApplet.P3D);
		assets.add(loadImage("Player.png"));
		assets.add(loadImage("Melee.png"));
		assets.add(loadImage("PlayerAttack.png"));
		backgrounds = loadImage("backgroundtest.png");
		lvl = OptionPanel.level;
		initLevel("Levels" + fileSeparator + "Level " + lvl + ".txt");



		spawnNewPlayer();
	}
 
	// The statements in draw() are executed until the 
	// program is stopped. Each statement is executed in 
	// sequence and after the last line is read, the first 
	// line is executed again.
	public void draw() {

		// drawing stuff
		if (lvl != OptionPanel.level) {
			setupNewLevel();
		}
		
		
		background(0,0,0);
		
		
		frameCount++;
		
		fill(255,0,0);
		textSize(20);
		text("FPS: " + averageFPS,0,30);
		fill(225);
		text("Lives: " + player.getLives(),0, 50);
		textSize(10);
		// scaling
		float ratioX = (float)width/DRAWING_WIDTH;
		float ratioY = (float)height/DRAWING_HEIGHT;
		scale(ratioX, ratioY);
		pushMatrix();
		// do acutall in game world drawing stuff here
		double xoff = DRAWING_WIDTH/2 - player.getx();
		this.translate((float)(xoff), 0);
		screenRect = new Rectangle((int)(player.getx()-DRAWING_WIDTH/2),0,DRAWING_WIDTH,DRAWING_HEIGHT);
		fill(100);
		if(player.intersects((Rectangle2D) goal)) {
			if(lvl == 1) {
				OptionPanel.level = 2;
			}

			else if(lvl == 2) {
				OptionPanel.level = 3;
			}

			else if(lvl == 3) {
				OptionPanel.level = 4;
			}
			else if(lvl == 4)
			{
				OptionPanel.level  = 1;
			}
		}
		Rectangle gl = (Rectangle) goal;
		pushStyle();
		fill(0,255,0);
		this.rect(gl.x,gl.y,gl.width,gl.height);
		popStyle();
		for (Shape s : obstacles) {
			if (s instanceof Rectangle && s.intersects(screenRect)) {
				Rectangle r = (Rectangle)s;

				rect(r.x,r.y,r.width,r.height);
			}
		}
		
		
		
		
		pushStyle();
		fill(0,0,255);
		
		
		
		
		
		
		
		for(Booster b : boosters) {
			if(b != null) {
				b.draw(this);
			}
		}
		popStyle();

	
		
		
		
		
		
		
		
		
		
		for(int i = 0; i < bullets.size(); i++) {
			if(bullets.get(i) != null && bullets.get(i).getIsDead()) {
				bullets.remove(i);
			}
			else {
				bullets.get(i).move();
			}
		}
		
		
		
		
		
		
		
		
		for(Bullet b : bullets) {
			for(MeleeEnemy me : meleeEnemies) {
				if(me.intersects(b)) {
					me.damaged(b.getDamage());
					b.setIsDead(true);

				}
			}

		}

		
		
		
		for(Bullet b : bullets) {
			if(b!=null && !b.getIsDead()) {
				b.draw(this);
				
			}
		}
		

		player.draw(this);
		
		
		
		
		
		
		
		for(MeleeEnemy me : meleeEnemies) {
			me.draw(this);
		}
		popMatrix();
		



		pushStyle();
		// do some untranslated ui drawing here
		// xp bar
		float xpBarX;
		float xpBarY;
		fill(90,90,90);
		rect(0,0,DRAWING_WIDTH,10);
		fill(255, 238, 53);
		double pxp = player.getXP();	
		double needed = player.getNeededEXP();
		double ratio = pxp / needed;
		rect(0,0,(float)(120+((DRAWING_WIDTH-120)*ratio)),10);
		fill(0);
		text("EXP : " + player.getXP() + " / " + (int)player.getNeededEXP()+".0", 10, 9);


		

		// ability 1

		for(int i = 0; i < 2; i++) {

			fill(255,255,255,200);
			stroke(0);
			strokeWeight(2);

			float cdRatio = (float)(1 - ((1.0*player.getCooldowns(i)) / player.getTechCD(i)));
			if(cdRatio < 1) {
				fill(155,155,155,200);
				rect((i+1)*DRAWING_WIDTH/6, DRAWING_HEIGHT/12, 50, 50);
			}
			else {
				fill(255,255,255,200);
				rect((i+1)*DRAWING_WIDTH/6, DRAWING_HEIGHT/12, 50, 50);
			}
			// ability 2

			// some cd math;
			// total perimeter = 50*4 = 200
			// total cd time = t1cd;
			// 1 unit of cd = 200/t1cd in length





			// 5 lines
			// l1 from topmid to topright; 
			// l2
			// l3
			// l4
			// l5

			float l1 = (float)(1.0/8);
			float l2 = (float)(3.0/8);
			float l3 = (float)(5.0/8);
			float l4 = (float)(7.0/8);
			float l5 = (float)(8.0/8);
			stroke(255, 238, 53);
			if(cdRatio < l1) {
				line((i+1)*DRAWING_WIDTH/6+25, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 25 + 25*(cdRatio/l1), DRAWING_HEIGHT/12);
			}
			else if(cdRatio < l2) {
				line((i+1)*DRAWING_WIDTH/6+25, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, 50*((cdRatio-l1)/(l2-l1)) + DRAWING_HEIGHT/12);
			}
			else if(cdRatio < l3) {
				line((i+1)*DRAWING_WIDTH/6+25, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, 50 + DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12+50,(i+1)*DRAWING_WIDTH/6 + 50*(1-((cdRatio-l2)/(l3-l2))), 50 + DRAWING_HEIGHT/12);
			}
			else if(cdRatio < l4) {
				line((i+1)*DRAWING_WIDTH/6+25, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, 50 + DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12+50,(i+1)*DRAWING_WIDTH/6, 50 + DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6, DRAWING_HEIGHT/12+50,(i+1)*DRAWING_WIDTH/6, 50 + DRAWING_HEIGHT/12 - 50*(cdRatio-l3)/(l4-l3));
			}
			else if(cdRatio < l5) {
				line((i+1)*DRAWING_WIDTH/6+25, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6 + 50, 50 + DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6+50, DRAWING_HEIGHT/12+50,(i+1)*DRAWING_WIDTH/6, 50 + DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6, DRAWING_HEIGHT/12+50,(i+1)*DRAWING_WIDTH/6, DRAWING_HEIGHT/12);
				line((i+1)*DRAWING_WIDTH/6, DRAWING_HEIGHT/12,(i+1)*DRAWING_WIDTH/6+25*(cdRatio-l4)/(l5-l4),DRAWING_HEIGHT/12);
			}
			else {
				noFill();
				rect((i+1)*DRAWING_WIDTH/6,DRAWING_HEIGHT/12,50,50);

			}
		}
		





		fill(225);
		
		text("Dash",12+DRAWING_WIDTH/6, -4 + DRAWING_HEIGHT/12);
		text("Launch",7 + 2*DRAWING_WIDTH/6, -4 + DRAWING_HEIGHT/12);
		

		fill(0);
		textSize(50);
		text('Q',4 + DRAWING_WIDTH/6, 41 + DRAWING_HEIGHT/12);
		text('W',4 + 2*DRAWING_WIDTH/6, 43 + DRAWING_HEIGHT/12);
		popStyle();
		
		if(player.getComboCount()>0)
		{
			g.fill(225);
			g.textSize(30);
			g.text("COMBO: " + player.getComboCount(), (float)500 ,(float)100 );
		}
		
		
		g.noFill();
		g.stroke(225);
		g.rect( DRAWING_WIDTH-200, (float)DRAWING_HEIGHT/10-10, (float)3*130/2, (float)15);
		g.fill(255,158,0);
		g.rect(DRAWING_WIDTH-200, (float)DRAWING_HEIGHT/10-10, (float) (3*130/2*player.getCBW()/player.getTBW()), (float)15);
		
		
		textSize(20);
		text("Rage: "+ player.getCBW(), DRAWING_WIDTH-175, (float)DRAWING_HEIGHT/10 - 20);	
		
		
		
		if(tSinceLast>0) {
			tSinceLast--;
		}
		 
		if (isPressed(KeyEvent.VK_R)) {
			
			if(tSinceLast == 0 && !player.energyReplenish()) {
				
				
				bullets.add(new Bullet(player.x, player.y+player.height/2 , 10.0 , player.getLevel()*10.0));
				tSinceLast = 30;
				player.energyDepletion(10);

			
			}
			 
		}
		if (isPressed(KeyEvent.VK_F)) {
			
			player.finisher(meleeEnemies);

			
		}
		if (isPressed(KeyEvent.VK_LEFT)) {
			player.walk(-1);
		}
		if (isPressed(KeyEvent.VK_RIGHT)) {
			player.walk(1);
		}
		if (isPressed(KeyEvent.VK_UP)) {
			if(jumpRelease || player.getOnGround()) {
				if(player.getCanJump() && player.getDy() >= -100) {
					player.jump();
					jumpRelease = false;
				}
			}
		}
		else {
			jumpRelease = true;
		}
		if(isPressed(KeyEvent.VK_Q)) {
			player.useTechOne();
		}
		else {
			player.endShield();
		} 
		
		if(isPressed(KeyEvent.VK_W)) {
			player.useTechTwo(meleeEnemies);

		}
		
		if(isPressed(KeyEvent.VK_E))
		{
			if(attackRelease) {
				player.attack(meleeEnemies);
				attackRelease = false;
			}
		}
		else
		{
			attackRelease = true;
			player.stopAttacking();
		}
		
		if(isPressed(KeyEvent.VK_A)||isPressed(KeyEvent.VK_S))
		{
			if(skillRelease) {
				if(isPressed(KeyEvent.VK_A)) {
					player.addPointsToOne();
					skillRelease = false;
				}
				else if(isPressed(KeyEvent.VK_S)) {
					player.addPointsToTwo();
					skillRelease = false;
				}

			}
		}
		else {
			skillRelease = true;
		}

		if (isPressed(KeyEvent.VK_T)) {
			if(!player.energyReplenish() && !player.getOnGround())	
			{	timeSpeed = 0.025;
				
				player.energyDepletion(1);}
			else
			{
				timeSpeed = 1;
			}
		}
		else {
			timeSpeed = 1;
		} 

		TimeEntity.setTimeRate(timeSpeed);

		if (isPressed(KeyEvent.VK_DOWN)) {
				player.startShield();
		}
		else {
			player.endShield();
		} 

		for(MeleeEnemy me : meleeEnemies) {
			if(me.getIsDead()) {
				me.actions(player, obstacles.get(1));
				if(me.gety() < DRAWING_HEIGHT+200 && Math.abs(me.getDx()) +  Math.abs(me.getDy()) > 1) {
					me.act(obstacles, meleeEnemies);
				}
				
				me.moveToLocation(100, 10000);
			}
			
			else {
				me.actions(player, obstacles.get(1));
				me.regen();
				me.act(obstacles,meleeEnemies);
				if(me.gety() > DRAWING_HEIGHT)
				{
					me.damaged(1000);
					
				}
			}
		}

		player.act(obstacles,meleeEnemies);
		player.isInCombat(meleeEnemies);
		


		// check for booster collisions and accelerate
		for (Booster b : boosters) {
			if(b!=null) {
				double[] dims = b.getCBoxDimensions();
				if (player.doesCollideWith(dims[0], dims[1], (int)dims[2], (int)dims[3])) {
					player.accelerate(b.getXBounceAcceleration(player.getx(), player.gety()), b.getYBounceAcceleration(player.getx(), player.gety()));
				}
				else {}
			}
		}

		


		if(player.gety() > DRAWING_HEIGHT || player.isGameOver()) {
			player.lifeLost();
			if(player.getLives()>-1)
			{
				respawnPlayer();
			}
			else
			{
				spawnNewPlayer();
			}
		}

		total = System.nanoTime() - start;
		
		if(total > 1000000000) {
			averageFPS = frameCount;
			frameCount = 0;
			total = 0;
			start = System.nanoTime();
		}
		
		
	}

	public void setupNewLevel() {
		
		lvl = OptionPanel.level;
		boosters = new ArrayList<Booster>();
		obstacles = new ArrayList<Shape>();
		platforms = new ArrayList<Platform>();
		meleeEnemies = new ArrayList<MeleeEnemy>(); 
		bullets = new ArrayList<Bullet>();
		
		initLevel("Levels" + fileSeparator + "Level " + lvl + ".txt");
		respawnPlayer();
	}
	
	public void keyPressed() {
		keys.add(keyCode);
	}

	public void keyReleased() {
		while(keys.contains(keyCode))
			keys.remove(new Integer(keyCode));
	}

	public boolean isPressed(Integer code) {
		return keys.contains(code);
	}


}

