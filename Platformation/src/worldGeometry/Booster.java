package worldGeometry;

import java.awt.Color;
import java.awt.Graphics;

import processing.core.PApplet;

public class Booster {
	
	private double x = 500;
	private double y = 600;
	
	private int width = 25;
	private int height = 25;
	
	private int power = 10;
	private double strength = 800;
	
	public Booster(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Booster(double x, double y,double w, double h) {
		this.x = x;
		this.y = y;
		width = (int)w;
		height = (int)h;
	}
	
	
	public void draw(PApplet g) {
		g.rect((float)x, (float)y, (float)width, (float)height);	
	}
	
	public double getXBounceAcceleration(double px, double py) {
		
		double distance = Math.sqrt(Math.pow((px-x),2) + Math.pow((py-y), 2));
		double ratio = Math.atan((py - y) / (px - x));	
		if (px - x < 0) {
			return -1 * power/(power + distance) * strength * Math.cos(ratio);
		}
		else {
		return power/(power + distance) * strength * Math.cos(ratio);
		}
	}
	public double getYBounceAcceleration(double px, double py) {
		
		double distance = Math.sqrt(Math.pow((px-x),2) + Math.pow((py-y), 2));
		double ratio = Math.atan((py - y) / (px - x));	
		if (px - x < 0) {
			return -1 * power/(power + distance) * strength * Math.sin(ratio);
		}
		else {
		return power/(power + distance) * strength * Math.sin(ratio);
		}
	}
	
	public double[] getCBoxDimensions() {
		double[] dims = {x,y,width,height};
		return dims;
	}
	
	
	
}
