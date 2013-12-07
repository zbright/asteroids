package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class RogueShip extends Drawable implements AsteroidsObj {
	private long lastTurn;
	private int[] dirArray = new int[] {-1, 1};
	private int level;
	
	public RogueShip(int x, int y, int lev) {
		level = lev;
		
		position[0] = (int)(Math.random() * x);
		position[1] = (int)(Math.random() * y);
		
		angle = 2 * Math.PI * Math.random();
		
		Random random = new Random();
		
		velocity[0] = random.nextDouble() * level  * Math.cos(angle);
		velocity[0] = keepVariableWithinRange(velocity[0], -10, 10);
		
		velocity[1] = random.nextDouble() * level * Math.sin(angle);
		velocity[1] = keepVariableWithinRange(velocity[1], -10, 10);
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		int[] baseX = {20, -12, -4, -12}, baseY = {0, -10, 0, 10};  

		drawObject(g, baseX, baseY, Color.RED, true);
	}

	@Override
	public void standardMove(int width, int height) {
		if(System.currentTimeMillis() - lastTurn > 2000) {
			Random random = new Random();
			
			angle += (dirArray[random.nextInt(2)] * .25);
			angle = keepVariableWithinRange(angle, 0, (2*Math.PI));
			
			lastTurn = System.currentTimeMillis();
		}
		
		position[0] += velocity[0] + level * Math.cos(angle);
		position[0] = keepVariableWithinRange(position[0], 0, width);
		
		position[1] += velocity[1] + level * Math.sin(angle);
		position[1] = keepVariableWithinRange(position[1], 0, height);
	}

	public boolean checkForCollision(Bullets bullet) {
		double distance = getMiddle().distance(bullet.getMiddle());
		if(distance > 15)
			return false;
		
		return true;
	}

}
