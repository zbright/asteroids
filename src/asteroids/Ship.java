package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Ship extends Drawable implements AsteroidsObj {
	private boolean isPlayerTwo;
	public boolean upPress = false, turnPress = false, shotPress = false;
	private AngleMultiplier rotateDir = AngleMultiplier.NONE;
	private int shotCount = 0;
	public int lives;
	public int score;
	public boolean isDead = false;
	public Color color = Color.DARK_GRAY;
	
	public Ship(int x, int y, double ang, boolean isSecond)
	{
		super();
		position[0] = x;
		position[1] = y;
		angle = ang;
		isPlayerTwo = isSecond;
		lives = 3;
		score = 0;
	}
	
	public int getPlayer() {
		if(isPlayerTwo)
			return 2;
		return 1;
	}
	
	public void draw(Graphics g){
		int[] baseX = {20, -12, -4, -12}, baseY = {0, -10, 0, 10};  

		drawObject(g, baseX, baseY, color, false);
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

	public boolean died(int x, int y) {
		position[0] = x;
		position[1] = y;
		
		lives--;
		
		if(lives <= 0) {
			isDead = true;
			return true;
		}
		return false;
	}

	public void nextLevel(int x, int y, double ang) {
		lives = 3;
		position[0] = x;
		position[1] = y;
		angle = ang;
	}
	
	public boolean checkForCollision(Bullets bullet) {
		double distance = getMiddle().distance(bullet.getMiddle());
		if(distance > 15)
			return false;
		
		return true;
	}
}
