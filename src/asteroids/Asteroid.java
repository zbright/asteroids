package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Asteroid extends Drawable implements AsteroidsObj {
	private AsteroidLevel astLevel = AsteroidLevel.NONE;
	
	public Asteroid(int x, int y, int level, AsteroidLevel asteroidLevel)
	{
		position[0] = (int)(Math.random() * x);
		position[1] = (int)(Math.random() * y);
		
		angle = 2 * Math.PI * Math.random();
		
		Random random = new Random();
		
		velocity[0] = (random.nextInt(2 + level) - ((int)level / 2)) * Math.cos(angle);
		velocity[0] = keepVariableWithinRange(velocity[0], -10, 10);
		
		velocity[1] = (random.nextInt(2 + level) - ((int)level / 2)) * Math.sin(angle);
		velocity[1] = keepVariableWithinRange(velocity[1], -10, 10);
		
		astLevel = asteroidLevel;
	}

	@Override
	public void draw(Graphics g) {
		int[] baseX = {-60,-20,20,60,60,20,-20,-60}, baseY = {20,60,60,20,-20,-60,-60,-20};  
		
		Color color = Color.GRAY;
		
		drawObject(g, baseX, baseY, color);
	}

	@Override
	public void standardMove(int width, int height) {
		position[0] += velocity[0];
		position[0] = keepVariableWithinRange(position[0], 0 - astLevel.index, width + astLevel.index / 2);
		
		position[1] += velocity[1];
		position[1] = keepVariableWithinRange(position[1], 0 - astLevel.index, height + astLevel.index / 2);	
	}
	
	public enum AsteroidLevel {
	    BIG(120), SMALL(60), NONE(0);
	    
	    private final int index;   

	    AsteroidLevel(int index) {
	        this.index = index;
	    }

	    public int index() { 
	        return index; 
	    }

	}
}
