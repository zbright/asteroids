package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Ship { //implements AsteroidsObj
	private double xPos, yPos, angle, accelerationConstant = .25;
	private boolean isPlayerTwo;
	private double[] velocity = {0, 0};
	private boolean velocityUpdated = false, angleUpdated = false;

	public Ship(int x, int y, double ang, boolean isSecond)
	{
		xPos = x;
		yPos = y;
		angle = ang;
		isPlayerTwo = isSecond;
	}
	
	public void draw(Graphics g){
		int[] newX = new int[4], newY = new int[4];
		int[] baseX = {14, -10, -6, -10}, baseY = {0, -8, 0, 8};  
		
		if(!isPlayerTwo)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.MAGENTA);
		
		for (int i = 0; i < 4; i++) {
			newX[i] = (int) (baseX[i] * Math.cos(angle) - baseY[i] * Math.sin(angle) + xPos + .5); 
			newY[i] = (int) (baseX[i] * Math.sin(angle) + baseY[i] * Math.cos(angle) + yPos + .5);
		}
		
		g.fillPolygon(newX, newY, 4);
	}

	public void move(int width, int height) {
		angleUpdated = false;
		velocityUpdated = false;
		
		xPos += velocity[0];
		yPos += velocity[1];
		
		xPos = keepVariableWithinRange(xPos, 0, width);
		yPos = keepVariableWithinRange(yPos, 0, height);
		
		velocity[0] = velocity[0] * .95;
		velocity[1] = velocity[1] * .95;
	}
	
	public void updateAcceleration() {
		if(!velocityUpdated)
		{
			velocity[0] += accelerationConstant * Math.cos(angle);
			velocity[1] += accelerationConstant * Math.sin(angle);
			velocityUpdated = true;
		}
	}
	
	public void updateAngle(AngleMultiplier angMult) {
		if(!angleUpdated)
		{
			angle += (angMult.index() * .1);
			angleUpdated = true;
			
			angle = keepVariableWithinRange(angle, 0, (2*Math.PI));
		}
	}
	
	private double keepVariableWithinRange(double val, double min, double max) {
		if(val > max)
			val -= max;
		else if(val < min)
			val += max;
		
		return val;
	}
	
	public enum AngleMultiplier {
	    RIGHT (1), LEFT (-1);
	    
	    private final int index;   

	    AngleMultiplier(int index) {
	        this.index = index;
	    }

	    public int index() { 
	        return index; 
	    }

	}
}
