package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Bullets extends Drawable implements AsteroidsObj, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6130812993056887800L;
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
	
	public Bullets(AlienShip alien, Ship ship)
	{
		double[] alienPosition = alien.getPosition();
		
		position[0] = alienPosition[0];
		position[1] = alienPosition[1];
		
		double alienX = alien.getMiddle().getX();
		double alienY = alien.getMiddle().getY();
		double shipX = ship.getMiddle().getX();
		double shipY = ship.getMiddle().getY();
		
		double dX = shipX - alienX;
		double dY = shipY - alienY;
		
		angle = Math.atan2(dY, dX);
		
		velocity[0] = (21 * Math.cos(angle));
		velocity[1] = (21 * Math.sin(angle));
		
		alien.setShotCountOrReset(false); //increment shot count
	}

	@Override
	public void draw(Graphics g) {
		if(playerOrigin == 0)
			g.setColor(Color.RED);
		else
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
