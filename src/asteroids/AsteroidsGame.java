package asteroids;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

public class AsteroidsGame extends Applet implements Runnable, KeyListener{
	Thread thread;
	static int screenWidth;
	static int screenHeight;
	private List<Bullets> bullets;
	private List<Asteroid> asteroids;
	private Ship playerOneShip;
	private Ship playerTwoShip;
	private boolean isMultiplayer;
	
	public AsteroidsGame() {
		super();
		addKeyListener(this);
		
		bullets = new ArrayList<Bullets>();
		asteroids = new ArrayList<Asteroid>();
		
		isMultiplayer = false;
		
		if(isMultiplayer){
			playerOneShip = new Ship((3*screenWidth/4), screenHeight/2, 0, false);
			playerTwoShip = new Ship(screenWidth/4, screenHeight/2, Math.PI, true);
		}
		else
			playerOneShip = new Ship(screenWidth/2, screenHeight/2, 0, false);
		
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);
		
		for(Bullets b : bullets)
			b.draw(g);
		
		for(Asteroid a : asteroids)
			a.draw(g);
		
		//Draw the first players ship and score/lives
		playerOneShip.draw(g);
		//Check if multiplayer is enabled
		//If it is, draw second players ship and score/lives
		
		if(isMultiplayer)
			playerTwoShip.draw(g);

	}
	
	public void run() {
		for (;;){
			long startTime = System.currentTimeMillis();
			
			playerOneShip.move(screenWidth, screenHeight);
			if(isMultiplayer)
				playerTwoShip.move(screenWidth, screenHeight);
			repaint();
		
			
			try {
				long endTime = System.currentTimeMillis();
				if (25 - (endTime - startTime) > 0)
					Thread.sleep(25 - (endTime - startTime));
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void update(Graphics gfx) {
		paint(gfx);
	}

	public void keyPressed(KeyEvent event) {
		KeyPressRelease(event, true);
	}
	
	public void keyReleased(KeyEvent event) {
		KeyPressRelease(event, false);
	}

	public void keyTyped(KeyEvent event) {
		//Nothing needed here
	}
	
	private void KeyPressRelease(KeyEvent event, boolean press) {
		int keyCode = event.getKeyCode();
		
		switch(keyCode)
		{
			//Player 1 movements
			case(KeyEvent.VK_UP):
				playerOneShip.updateAcceleration();
				break;
			case(KeyEvent.VK_LEFT):
				playerOneShip.updateAngle(Ship.AngleMultiplier.LEFT);
				break;
			case(KeyEvent.VK_RIGHT):
				playerOneShip.updateAngle(Ship.AngleMultiplier.RIGHT);
				break;
			case(KeyEvent.VK_EQUALS): //Firing
				break;
			
			//Player 2 movements
			case(KeyEvent.VK_W):
				break;
			case(KeyEvent.VK_S):
				break;
			case(KeyEvent.VK_D):
				break;
			case(KeyEvent.VK_SPACE):
				break;
			
			case(KeyEvent.VK_ENTER): //Start game after pause
				break;
			case(KeyEvent.VK_ESCAPE): //Bring up pause menu
				break;
			case(KeyEvent.VK_END): //Exit
				break;
		}
		
	}
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height - 50;
		
		AsteroidsGame asteroids = new AsteroidsGame();
		asteroids.spinNewThread();
		JFrame frame = new JFrame("Asteroids");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(asteroids);
		frame.setSize(screenWidth, screenHeight);
		frame.setVisible(true);
		
		asteroids.start();
	}

	private void spinNewThread() {
		thread = new Thread(this);
		thread.start();
	}
}
