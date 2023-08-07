package entities;


import java.awt.*;
import java.util.*;

import processing.core.PImage;
import worldGeometry.Platform;

public class Mario extends Sprite {

	public static final int MARIO_WIDTH = 40;
	public static final int MARIO_HEIGHT = 60;
	private double dt = 1 / 60.0;
	private double dx, dy;
	private double oldDx, oldDy;
	private double ddx, ddy;
	private int maxDx = 400;
	private int maxDy = 240;
	private boolean isTouchingGround;
	private boolean isMoving;
	private double ticksFromZeroToHalf = 4.0;
	private double ticksFromHalfToFull = 8.0;
	private double ticksToStop = 1.0;

	private double fricMod = 0.5;
	
	public Mario(PImage img, int x, int y) {
		super(img, x, y, MARIO_WIDTH, MARIO_HEIGHT);
	}

	// METHODS
	public void walk(int dir) {
		isMoving = true;
		// WALK!
		double accelAmt = 0;
		isMoving = true;
		if (dir*dx >= 0 && dir*dx <=maxDx/2) {
			accelAmt = maxDx / ticksFromZeroToHalf;
		}
		else if (dir*dx >= maxDx/2 && dir*dx <= maxDx) {
			double accelerationFromZeroToHalf = maxDx / 2 / ticksFromZeroToHalf;
			double accelerationCoefficient = accelerationFromZeroToHalf / ticksFromHalfToFull;
			double tickCount = ticksFromHalfToFull + Math.sqrt(Math.pow(ticksFromHalfToFull, 2) / (maxDx / 2) * (maxDx-dx));        
			accelAmt =  (accelerationCoefficient * tickCount) + accelerationFromZeroToHalf;
			accelAmt *= dir;
		}
		else if (dir*dx >= -maxDx && dir* dx <=0) {
			accelAmt = maxDx / ticksToStop;
			accelAmt *= dir;
		}


		if (isTouchingGround) {

		}
		else {
			accelAmt = accelAmt * 2 / 3;
		}
		
		
		
		if (accelAmt + dx > maxDx) {accelAmt = maxDx - dx;}
		else if(accelAmt + dx < -maxDx) {accelAmt = -maxDx - dx;}
		accelerate(accelAmt, 0); 
	}

	public void jump() {
		// JUMP!
		if(isTouchingGround){
			if(dy > -maxDy && dy < maxDy) {
				accelerate(0, -640);
			}
			isTouchingGround = false;
		}
		
	}

	public void act(ArrayList<Platform> obstacles) {
		// FALL (and stop when a platform is hit)
		boolean inside = false;
		for(Platform p : obstacles) {
			double[] dims = p.getCBoxDimensions();
			if(intersects(dims[0],dims[1] , dims[2], dims[3])) {
				inside = true;
			}
		}
		if(inside) {
			isTouchingGround = true;
		}
		else {	
			
		}
		accelerate(0, 1440*dt);
		
		int[] colDir = null;
		Platform ycolliding = wouldBeY(obstacles);
		if(ycolliding != null) {
			colDir = ycolliding.getColDir(x, y);
			super.moveToLocation(x, -1*MARIO_HEIGHT + ycolliding.getCBoxDimensions()[1] + ((0.5)*ycolliding.getCBoxDimensions()[3]*colDir[1]));
			if(colDir[1] == -1 && dy > 0) {
				isTouchingGround = true;
			}
			accelerate(0,-dy);
		}
		
		
		
		applyFriction();
		isMoving = false;
		
		if(ycolliding == null) {
			super.moveByAmount(dt * (oldDx + ((ddx/2) * dt)), dt * (oldDy + ((ddy/2) * dt)));
		}
		else {
			super.moveByAmount(dt * (oldDx + ((ddx/2) * dt)), 0);
		}
		
		oldDx = this.dx;
		oldDy = this.dy;
		ddx = 0;
		ddy = 0;
	}
	
	private void accelerate (double dx, double dy) {
		// This is a simple acceleate method that adds dx and dy to the current velocity.
		this.dx += dx;
		this.dy += dy;

		// This part of the method is used to remember how much the player accelerated in the frame
		// It is only useful for calculating distance moved :)
		ddx += dx;
		ddy += dy;
	}
	
	private Platform wouldBeX(ArrayList<Platform> obstacles) {
		Platform colliding = null;
		for(Platform p : obstacles) {
			double[] dims = p.getCBoxDimensions();
			Rectangle r = new Rectangle((int)dims[0],(int)dims[1], (int)dims[2], (int)dims[3]);
			if(r.intersects(x + dt * (oldDx + ((ddx/2) * dt)), y, MARIO_WIDTH, MARIO_HEIGHT)) {
				colliding = p;
			}
		}
		return colliding;
	}
	private Platform wouldBeY(ArrayList<Platform> obstacles) {
		Platform colliding = null;
		for(Platform p : obstacles) {
			double[] dims = p.getCBoxDimensions();
			Rectangle r = new Rectangle((int)(dims[0] - dims[2]/2),(int)(dims[1] - dims[3]/2), (int)dims[2], (int)dims[3]);
			if(r.intersects(x, y + dt * (oldDy + ((ddy/2) * dt)), MARIO_WIDTH, MARIO_HEIGHT)) {
				colliding = p;
			}
		}
		return colliding;
	}
	private void applyFriction() {
		if (isMoving) {
			if (isTouchingGround) {
				accelerate(-dx/12*fricMod,0); 
			}
			else {
				accelerate(-dx/12*fricMod,0); 	
			}
		}
		else {
			if (isTouchingGround) {
				accelerate(-dx/2*fricMod,0); 
			}
			else {
				accelerate(-dx/12*fricMod,0); 	
			}
		}
	}

	

	
}
