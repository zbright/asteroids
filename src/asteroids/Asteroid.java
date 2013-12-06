package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends Drawable implements AsteroidsObj {
	public Asteroid(int x, int y, double ang)
	{
		position[0] = x;
		position[1] = y;
		angle = ang;
	}

	@Override
	public void draw(Graphics g) {
		int[] baseX = {-30,-10,10,30,30,10,-10,-30}, baseY = {10,30,30,10,-10,-30,-30,-10};  
		
		Color color = Color.WHITE;
		
		drawObject(g, baseX, baseY, color);
	}

	@Override
	public void standardMove(int width, int height) {
		// TODO Auto-generated method stub
		
	}
}
