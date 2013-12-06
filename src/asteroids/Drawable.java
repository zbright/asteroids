package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Drawable {
	protected double angle;
	protected double[] velocity = {0, 0}, position = {0, 0};
	
	protected void drawObject(Graphics g, int[] xPoints, int[] yPoints, Color color)
	{
		int len = xPoints.length;
		int[] newX = new int[len], newY = new int[len];
		
		for(int i=0 ; i<len ; i++) {
			newX[i] = (int)(xPoints[i] * Math.cos(angle) - yPoints[i] * Math.sin(angle) + position[0] + 0.5);
			newY[i] = (int)(xPoints[i] * Math.sin(angle) + yPoints[i] * Math.cos(angle) + position[1] + 0.5);
	    }
	    g.setColor(color);
	    g.drawPolygon(newX, newY, len);
	}
}
