package asteroids;

import java.awt.Color;
import java.awt.Graphics;

public class Ship implements AsteroidsObj{
	private int xPos, yPos;
	private double angle;
	private boolean isPlayerTwo;

	public Ship(int x, int y, double ang, boolean isSecond)
	{
		xPos = x;
		yPos = y;
		angle = ang;
		isPlayerTwo = isSecond;
	}
	
	@Override
	public void draw(Graphics g){
		int[] newX = new int[4], newY = new int[4];
		int[] baseX = {14, -10, -6, -10}, baseY = {0, -8, 0, 8};  
		
		if(!isPlayerTwo)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.MAGENTA);
		
		for (int i = 0; i < 4; i++) {
			newX[i] = (int) (baseX[i] * Math.cos(angle) - baseY[i] * Math.sin(angle) + xPos + .5); 
			newY[i] = (int) (baseX[i] * Math.sin(angle) + baseY[i] * Math.cos(angle) + yPos + .5);
		}
		
		g.fillPolygon(newX, newY, 4);
	}

	@Override
	public void move(int width, int height) {
		// TODO Auto-generated method stub
		
	}
}
