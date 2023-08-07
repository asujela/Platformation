package entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PImage;

public class RangedEnemy extends Enemy {
	
	
	private Rectangle2D sightBox;

	
	public RangedEnemy(PImage img, int x, int y, double level, double statPoints, PApplet marker) {
		super(img, x, y, level, statPoints, marker);
		// TODO Auto-generated constructor stub
	}

	
	public void actions(Player p, Shape plat)
	{
		// gotta reset the sight box every time because the enemy moves
		sightBox = new Rectangle((int)x-200,(int)y-220,(int)PLAYER_WIDTH+400, (int)PLAYER_HEIGHT+250);
		if(sightBox.contains(p) )
		{
			this.moveToPlayer(p);
		}
		
	
	}

	private void moveToPlayer(Player p)
	{
		if(p.getx() < this.getx())
		{
			
			walk(-1);
			accelerate(-super.getDx()/4,0);
			if(p.gety() > this.gety())
			{
				jump();
			}
			
			
		}
		else if(p.getx() > this.getx())
		{
			walk(1);
			accelerate(-super.getDx()/4,0);
			
		}
	}


}
