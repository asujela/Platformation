package entities;

import java.awt.Shape;

import javax.management.timer.Timer;

import processing.core.PApplet;
import processing.core.PImage;

public class Enemy extends Player implements Damageable {

	


	private boolean replenishing;
	private boolean isDead;
	

	private double level;
	private double attackStat, attackSecond;
	private double defStat;
	private double EXP;
	private int c = 0;
	private boolean hasBeenHitByDash;


	public Enemy(PImage img, int x, int y, double level, double statPoints, PApplet marker) {
		super(img, x, y,level,0.5, marker);
		// TODO Auto-generated constructor stub
		replenishing = false;
		this.level = level;
		HPCalculation(statPoints);
		defCalculation(statPoints);
		attackCalculation(statPoints);
		EPCalculation(statPoints);
		isEnemy = true;
		EXP = level*10;
		attackSecond = 0;
	}
	
	
	
	private void HPCalculation(double statPoints)
	{
		this.setHP(statPoints + (level*40)+200);
		
	}
	
	
	
	private void EPCalculation(double statPoints)
	{
		this.setEP(statPoints + (level*20));
	}
	
	
	
	
	private void defCalculation(double statPoints)
	{
		defStat = statPoints + (level*10);
	}
	
	
	
	private void attackCalculation(double statPoints)
	{
		 attackStat = statPoints + (level*2);
		
	}
	

	public void knockedBack(double dx, double dy, Player p)
	{
		if(p.getCenterX()>this.getCenterX())
		{
			super.instantAccelerate(-dx, dy);
		}
		else
		{
			super.instantAccelerate(dx, dy);
		}
			
	}
	

	public void stunned(double strength) {	
		if(strength-1 > stunTicks) {
			stunTicks = (int) strength;
		}
			
	}

	
	public void attack(Player p)
	{	
			if(attackSecond%10== 0)
			{
				p.damaged(attackStat*TimeEntity.TIME_RATE);
			}
			
			
			attackSecond++;
			
	
		
	}

	@Override
	public double damaged(double damageTaken) {
		// TODO Auto-generated method stub
		
		super.damaged(damageTaken);
		
		if(getHP() <= 0) {
			setHP(0);
			disapear();
			attackStat = 0;
		}
				
		return super.damaged(damageTaken);
	}

	
	
	@Override
	public void regen() {
	}

	
	
	@Override
	public double getHP() {
		// TODO Auto-generated method stub
		return super.getHP();
	}

	
	
	@Override
	public double energyDepletion(double usedEP) {
		// TODO Auto-generated method stub
		
		return usedEP;
	}

	
	
	
	@Override
	public boolean energyReplenish() {
		// TODO Auto-generated method stub
		
			super.energyReplenish();
			return replenishing;
		
	}
	
	

	@Override
	public double getEP() {
		// TODO Auto-generated method stub
		return super.getEP();
	}
	
	public void setHasBeenHitByDash(boolean hasBeenHit) {
		hasBeenHitByDash = hasBeenHit;
	}
	public boolean getHasBeenHitByDash() {
		return hasBeenHitByDash;
	}
	
	public boolean ifReplenishing()
	{
		return replenishing;
	}

	public void draw(PApplet g)
	{
		super.draw(g);
	}

	public void action(Player player, Shape shape) {
		// TODO Auto-generated method stub
		//walk(0);
	}
	
	public double disapear()
	{
		isDead = true;
		return EXP;
	}
	
	public boolean getIsDead()
	{
		return isDead;
	}



}
