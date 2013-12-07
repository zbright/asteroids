package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class GravitationalObject extends Drawable implements AsteroidsObj {
	double width, height;
	
	public GravitationalObject(double x, double y)
	{
		width = x;
		height = y;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(((int)width / 2) - 15, ((int)height / 2) - 15, 30, 30);
		
		g.setColor(Color.DARK_GRAY);
		g.fillOval(((int)width / 2) - 10, ((int)height / 2) - 10, 20, 20);
		
		g.setColor(Color.BLACK);
		g.fillOval(((int)width / 2) - 5, ((int)height / 2) - 5, 10, 10);
	}

	@Override
	public void standardMove(int width, int height) {
		//This obj doesnt move at all
	}

}
