package entities;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import processing.core.PApplet;
import processing.core.PImage;
import worldGeometry.Platform;

public class MeleeEnemy extends Enemy {

	private Rectangle2D sightBox;
	private boolean moved ;
	private boolean gaveEXP;
	public MeleeEnemy( PImage img ,int x, int y, int lvl, int statPoint, PApplet marker)
	{
		super(img, x, y, lvl, statPoint, marker);

		sightBox = new Rectangle(x-200,y-10,PLAYER_WIDTH+400, PLAYER_HEIGHT+10);
		gaveEXP = false;

	}

	public void actions(Player p, Shape plat)
	{
		// gotta reset the sight box every time because the enemy moves
		sightBox = new Rectangle((int)x-220,(int)y-220,(int)PLAYER_WIDTH+420, (int)PLAYER_HEIGHT+250);
		if(super.getHP()<=0)
		{
			super.setHP(0);
			super.disapear();
		}
		if(super.getIsDead() == false)
		{
			if(sightBox.contains(p) )
			{
				this.moveToPlayer(p);
			}
			else
			{
				this.idleWalk(plat);
			}


			if(p.intersects(this) && !isStunned)
			{
				attack(p);
			}
		}
		else
		{
			if(gaveEXP == false)
			{
				p.obtainEXP(disapear());
				gaveEXP = true;
			}
		}
	}

	private void moveToPlayer(Player p)
	{
		if(isStunned) {
			
		}
		
		else {
			
			if(p.getx() < this.getx()) {

				walk(-1);
				accelerate(-super.getDx()/4,0);
				if(p.gety() > this.gety()) {
					//jump();
				}


			}
			else if(p.getx() > this.getx()) {
				walk(1);
				accelerate(-super.getDx()/4,0);

			}
		}
	}

	private void idleWalk(Shape plat)
	{

		Rectangle platform = plat.getBounds();

		double xPlat = platform.getX();
		double width = platform.getWidth();


		if(this.getX() > xPlat+4 && moved==false)
		{
			//walk(1);

		}
		else if(this.getX() < width-4 && moved == true)
		{
			//walk(-1);

		}
		else if(this.getX() <= xPlat)
		{
			moved = true;
		}
		else if(this.getX() >= width)
		{
			moved = false;
		}


	}







}
