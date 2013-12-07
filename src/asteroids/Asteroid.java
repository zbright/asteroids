package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
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

	//This constructor is used to create small asteroids
	public Asteroid(Asteroid asteroid, int level) {
		position[0] = asteroid.position[0];
		position[1] = asteroid.position[1];
		
		angle = 2 * Math.PI * Math.random();
		
		Random random = new Random();
		
		velocity[0] = random.nextInt(3) - 1 * level * Math.random() * Math.cos(angle);
		velocity[0] = keepVariableWithinRange(velocity[0], -10, 10);
		
		velocity[1] = random.nextInt(3) - 1 * level * Math.random() *  Math.sin(angle);
		velocity[1] = keepVariableWithinRange(velocity[1], -10, 10);
		
		astLevel = AsteroidLevel.SMALL;
	}

	@Override
	public void draw(Graphics g) {
		int[] baseX, baseY; 
		
		if(astLevel == AsteroidLevel.BIG) {
			baseX = new int[] {-60,-20,20,60,60,20,-20,-60};
			baseY = new int[] {20,60,60,20,-20,-60,-60,-20};
		}
		else if(astLevel == AsteroidLevel.SMALL) {
			baseX = new int[] {-30,-10,10,30,30,10,-10,-30};
			baseY = new int[] {10,30,30,10,-10,-30,-30,-10};
		}
		else
			return;
		
		Color color = Color.GRAY;
		
		drawObject(g, baseX, baseY, color, false);
	}

	@Override
	public void standardMove(int width, int height) {
		position[0] += velocity[0];
		position[0] = keepVariableWithinRange(position[0], 0 - astLevel.index / 2, width + astLevel.index / 2);
		
		position[1] += velocity[1];
		position[1] = keepVariableWithinRange(position[1], 0 - astLevel.index / 2, height + astLevel.index / 2);	
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

	public boolean checkForCollision(Bullets bullet) {
		double distance = getMiddle().distance(bullet.getMiddle());
		if(distance > astLevel.index / 2)
			return false;
		
		return true;
	}

	public boolean checkForCollision(Ship ship) {
		double distance = getMiddle().distance(ship.getMiddle());
		if(distance > astLevel.index / 2 + 15 || ship.isDead)
			return false;
		
		return true;
	}
	
	public AsteroidLevel getAstLevel() {
		return astLevel;
	}

	public void split(List<Asteroid> asteroids, int level) {
		for(int i = 0; i < 3; i++) {
			asteroids.add(new Asteroid((int)position[0], (int)position[1], level, astLevel));
		}
		
	}
}
