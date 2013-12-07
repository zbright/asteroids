package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Bullets extends Drawable implements AsteroidsObj{
	public double distanceFromShootLocation = 0;
	public int playerOrigin = 0;
	
	public Bullets(Drawable ship)
	{
		double[] shipVelocity = ship.getVelocity();
		double[] shipPosition = ship.getPosition();
		
		position[0] = shipPosition[0];
		position[1] = shipPosition[1];
		velocity[0] = shipVelocity[0] + (21 * Math.cos(ship.getAngle()));
		velocity[1] = shipVelocity[1] + (21 * Math.sin(ship.getAngle()));
		
		if(ship instanceof Ship) {
			playerOrigin = ((Ship)ship).getPlayer();
			
			((Ship)ship).setShotCountOrReset(false); //increment shot count
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval((int)position[0], (int)position[1], 5, 5);
	}

	@Override
	public void standardMove(int width, int height) {
		distanceFromShootLocation += Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
		
		position[0] += velocity[0];
		position[0] = keepVariableWithinRange(position[0], 0, width);
		
		position[1] += velocity[1];
		position[1] = keepVariableWithinRange(position[1], 0, height);		
	}
}
