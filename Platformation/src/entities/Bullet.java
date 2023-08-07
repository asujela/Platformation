package entities;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PApplet;

public class Bullet extends Rectangle2D.Double{

	private double direction;
	private boolean isDead;
	private double damage;
	private int c = 0;
	
	public Bullet(double x, double y, double direction, double damage) {
		
		super(x,y,2,2);
		this.direction = direction;
		this.damage = damage;
		
	}
	
	
	public boolean getIsDead() {
		return isDead;
	}
	public void setIsDead(boolean dead) {
		isDead = dead;
	}
	
	
	public double getx() {
		return y;
	}
	public double gety() {
		return x;
	}
	
    public void damageEnemy(ArrayList<MeleeEnemy> meleeEnemies)
    {
    	for(int i = 0; i<meleeEnemies.size(); i++)
		{	
			if(meleeEnemies.get(i).intersects(this))
			{
				meleeEnemies.get(i).damaged(damage);
			}
		}
		
    	
    }
	public void act(ArrayList<MeleeEnemy> m)
	{
		damageEnemy(m);
	}
	public double getDamage() {
		if(c>30)
		{
			damage*=2;
		}
		return damage;
	}
	
	public void draw(PApplet g) {
		g.pushStyle();
		if(c<20)
		{
			g.fill(0,0,250);
		}
		else if(c>20&&c<30)
		{
			g.fill(250,0,250);
		}
		else if(c>30)
		{
			g.fill(250,0,0);
		}
				
		g.rect((float)x, (float)y, (float)20.0,(float)20.0);
		
		g.popStyle();
	}
	
	public void move() {
		x += direction;
		c++;
		if(c > 40) {
			isDead = true;
		}
	}
	
	
	
}
