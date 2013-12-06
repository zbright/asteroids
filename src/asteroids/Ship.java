package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Ship extends Drawable implements AsteroidsObj {
	private boolean isPlayerTwo;
	public boolean upPress = false, turnPress = false, shotPress = false;
	private AngleMultiplier rotateDir = AngleMultiplier.NONE;
	private int shotCount = 0;
	
	public Ship(int x, int y, double ang, boolean isSecond)
	{
		position[0] = x;
		position[1] = y;
		angle = ang;
		isPlayerTwo = isSecond;
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
	
	public int getPlayer() {
		if(isPlayerTwo)
			return 2;
		return 1;
	}
	
	public void draw(Graphics g){
		int[] baseX = {14, -10, -6, -10}, baseY = {0, -8, 0, 8};  
		
		Color color = Color.WHITE;
		
		if(isPlayerTwo)
			color = Color.MAGENTA;
		
		drawObject(g, baseX, baseY, color);
	}
	
	public void standardMove(int width, int height) {
		position[0] += velocity[0];
		position[1] += velocity[1];
		
		position[0] = keepVariableWithinRange(position[0], 0, width);
		position[1] = keepVariableWithinRange(position[1], 0, height);
		
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

	public void shoot() {
		shotPress = true;
	}
	
	public void stopShooting() {
		shotPress = false;
	}

	public int getShotCount() {
		return shotCount;
	}
	
	public void setShotCountOrReset(boolean reset) {
		if(!reset)
			shotCount++;
		else
			shotCount = 0;
	}
}
