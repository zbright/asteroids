package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Bullets implements AsteroidsObj{
	public double distanceFromShootLocation = 0;
	public int playerOrigin = 0;
	private double[] velocity = {0, 0}, position = {0, 0};
	
	public Bullets(Ship ship)
	{
		double[] shipVelocity = ship.getVelocity();
		double[] shipPosition = ship.getPosition();
		
		position[0] = shipPosition[0];
		position[1] = shipPosition[1];
		velocity[0] = shipVelocity[0] + (21 * Math.cos(ship.getAngle()));
		velocity[1] = shipVelocity[1] + (21 * Math.sin(ship.getAngle()));
		
		playerOrigin = ship.getPlayer();
		
		ship.setShotCountOrReset(false); //increment shot count
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
	
	private double keepVariableWithinRange(double val, double min, double max) {
		if(val > max)
			val -= max;
		else if(val < min)
			val += max;
		
		return val;
	}
}
