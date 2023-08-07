package worldGeometry;

import java.awt.Rectangle;
import java.awt.Shape;

public class Platform {

	private double width;
	private double height;
	private double x;
	private double y;

	public Platform(Shape s) {
		Rectangle r = s.getBounds();
		x = r.getCenterX();
		y = r.getCenterY();
		width = r.getWidth();
		height = r.getHeight();
		
	}

	public double[] getCBoxDimensions() {
		double[] dims = {x,y,width,height};
		return dims;
	}
	
	public int[] getColDir(double px, double py) {
		double xdif, ydif;
		// returns an int in the form: {x dir, y dir}
		int[] result = new int[2];
		xdif = px-x;
		ydif = py-y;
		if ( xdif > ydif) {
			if (xdif > 0) {
				// right
				result[0] = 1;
			}
			else {
				// left
				result[0] = -1;
			}
		}
		else {
			if (ydif > 0) {
				//down
				result[1] = 1;
			}
			else {
				// up
				result[1] = -1;
			}
			
		}
		return result;
	}

}
