package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public abstract class Drawable {
	protected double angle;
	protected double[] velocity = {0, 0}, position = {0, 0};
	
	protected void drawObject(Graphics g, int[] xPoints, int[] yPoints, Color color, boolean fill)
	{
		int len = xPoints.length;
		int[] newX = new int[len], newY = new int[len];
		
		for(int i=0 ; i<len ; i++) {
			newX[i] = (int)(xPoints[i] * Math.cos(angle) - yPoints[i] * Math.sin(angle) + position[0] + 0.5);
			newY[i] = (int)(xPoints[i] * Math.sin(angle) + yPoints[i] * Math.cos(angle) + position[1] + 0.5);
	    }
	    g.setColor(color);
	    
	    if(!fill)
	    	g.drawPolygon(newX, newY, len);
	    else
	    	g.fillPolygon(newX, newY, len);
	}
	
	protected double keepVariableWithinRange(double val, double min, double max) {
		if(val > max)
			val -= max;
		else if(val < min)
			val += max;
		
		return val;
	}
	
	protected Point2D getMiddle() {
		return new Point2D.Double(position[0], position[1]);
	}
	
	public double[] getVelocity() {
		return velocity;
	}
	
	public double[] getPosition() {
		return position;
	}
	
	public double getAngle()
	{
		return angle;
	}
}
