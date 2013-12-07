package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class AlienShip extends Drawable implements AsteroidsObj{
	private int level;
	public int livesLeft;
	public int shotCount = 0;
	
	public AlienShip(int x, int y, int lev) {
		level = lev;
		livesLeft = 3;
		
		position[0] = (int)(Math.random() * x);
		position[1] = (int)(Math.random() * y);
		
		Random random = new Random();
		velocity[0] = random.nextDouble() * level + 1;
		velocity[0] = keepVariableWithinRange(velocity[0], -10, 10);
	}
	
	@Override
	public void draw(Graphics g) {
		int[] baseX = {-30, -20, -10, -10, 10, 10, 20, 30, 20, -20}, baseY = {0, -10, -10, -16, -16, -10, -10, 0, 16, 16};  

		drawObject(g, baseX, baseY, Color.RED, true);
	}

	@Override
	public void standardMove(int width, int height) {
		position[0] += velocity[0];
		
		if(position[0] < 0 || position[0] > width)
			position[1] = Math.random() * height - 20;
		
		position[0] = keepVariableWithinRange(position[0], 0, width);
	}

	public boolean checkForCollision(Bullets b) {
		double distance = getMiddle().distance(b.getMiddle());
		if(distance > 30)
			return false;
		
		return true;
	}
	
	public boolean checkForCollision(Ship ship) {
		double distance = getMiddle().distance(ship.getMiddle());
		if(distance > 45)
			return false;
		
		return true;
	}

	public void setShotCountOrReset(boolean reset) {
		if(!reset)
			shotCount++;
		else
			shotCount = 0;
	}

}
