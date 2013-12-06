package asteroids;

import java.awt.Color;
import java.awt.Graphics;

import javax.sql.rowset.spi.SyncResolver;

public class Ship { //implements AsteroidsObj
	private double xPos, yPos, angle;
	private boolean isPlayerTwo;
	private double[] velocity = {0, 0};
	private boolean velocityUpdated = false, angleUpdated = false;
	public boolean upPress = false, turnPress = false;
	private AngleMultiplier rotateDir = AngleMultiplier.NONE;

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
		
		g.drawPolygon(newX, newY, 4);
	}

	public void move(int width, int height) {
		angleUpdated = false;
		velocityUpdated = false;
		
		if(turnPress)
		{
			angle += (rotateDir.index() * .1);
			angle = keepVariableWithinRange(angle, 0, (2*Math.PI));
		}
		if(upPress)
		{
			velocity[0] += .25 * Math.cos(angle);
			velocity[1] += .25 * Math.sin(angle);
		}
		
		xPos += velocity[0];
		yPos += velocity[1];
		
		xPos = keepVariableWithinRange(xPos, 0, width);
		yPos = keepVariableWithinRange(yPos, 0, height);
		
		velocity[0] = velocity[0] * .95;
		velocity[1] = velocity[1] * .95;
	}
	
	public void standardMove(int width, int height) {
		xPos += velocity[0];
		yPos += velocity[1];
		
		xPos = keepVariableWithinRange(xPos, 0, width);
		yPos = keepVariableWithinRange(yPos, 0, height);
		
		velocity[0] = velocity[0] * .95;
		velocity[1] = velocity[1] * .95;
	}
	
	public void accelerate(){
		velocity[0] += .25 * Math.cos(angle);
		velocity[1] += .25 * Math.sin(angle);
	}
	
	public void turn() {
		angle += (rotateDir.index() * .1);
		angle = keepVariableWithinRange(angle, 0, (2*Math.PI));
	}
	
	public void updateAcceleration() {
		upPress = true;
	}
	
	public void updateAngle(AngleMultiplier angMult) {
		turnPress = true;
		rotateDir = angMult;
	}
	
	public void stopAcceleration() {
		upPress = false;
	}
	
	public void stopTurning() {
		rotateDir = AngleMultiplier.NONE;
		turnPress = false;
	}
	
	private double keepVariableWithinRange(double val, double min, double max) {
		if(val > max)
			val -= max;
		else if(val < min)
			val += max;
		
		return val;
	}
	
	public enum AngleMultiplier {
	    RIGHT (1), LEFT (-1), NONE(0);
	    
	    private final int index;   

	    AngleMultiplier(int index) {
	        this.index = index;
	    }

	    public int index() { 
	        return index; 
	    }

	}
}
