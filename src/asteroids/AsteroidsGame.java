package asteroids;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

import asteroids.Asteroid.AsteroidLevel;

public class AsteroidsGame extends Applet implements Runnable, KeyListener{
	Thread thread;
	static int screenWidth;
	static int screenHeight;
	private List<Bullets> bullets;
	private List<Asteroid> asteroids;
	private Ship playerOneShip;
	private Ship playerTwoShip;
	private boolean isMultiplayer;
	private long lastShotTime1;
	private long lastShotTime2;
	private Image image;
	private Graphics graphics;
	private int level;
	private boolean playerOneGameOver = false;
	private boolean playerTwoGameOver = false;
	private boolean paused = true;
	
	public AsteroidsGame() {
		super();
		addKeyListener(this);
		
		level = 0;
		bullets = new ArrayList<Bullets>();
		asteroids = new ArrayList<Asteroid>();
		
		isMultiplayer = true;
	}

	public void paint(Graphics gGeneric) {
		Graphics2D g = (Graphics2D) graphics;
        RenderingHints render = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHints(render);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);
		
		for(Bullets b : bullets)
			b.draw(g);
		
		for(Asteroid a : asteroids)
			a.draw(g);
		
		Font font = new Font("Veranda", Font.PLAIN, 25);
		
		if(paused)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.WHITE);
			
		
		g.setFont(font);
        g.drawString("Level : " + level, screenWidth / 2, screenHeight - 50);
        
		//Draw the players ship and score/lives
		if(playerOneShip != null) {
			if(!paused)
				playerOneShip.color = Color.CYAN;
			else
				playerOneShip.color = Color.DARK_GRAY;
			
			g.setColor(playerOneShip.color);			
			g.drawString("Player 1: " + "Lives : " + playerOneShip.lives + " Score : " + playerOneShip.score, 0, screenHeight - 75); 
			
			if(!playerOneGameOver)
				playerOneShip.draw(g);
		}
		
		if(isMultiplayer && playerTwoShip != null) {
			if(!paused)
				playerTwoShip.color = Color.MAGENTA;
			else
				playerTwoShip.color = Color.DARK_GRAY;
			
			g.setColor(playerTwoShip.color);
			
			g.drawString("Player 2: " + "Lives : " + playerTwoShip.lives + " Score : " + playerTwoShip.score, 0, screenHeight - 50);
			
			if(!playerTwoGameOver)
				playerTwoShip.draw(g);
		}
		
		gGeneric.drawImage(image, 0, 0, this);
	}
	
	public void run() {
		for (;;){
			
				if(asteroids.isEmpty()) {
					goToNextLevel();
				}
				
				handleKeyboardInputs();
				
				if(!paused) {
					checkObjectsForCollision();
				}
				
			repaint();
			
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void checkObjectsForCollision() {
		ArrayList<Asteroid> asteroidsToDelete = new ArrayList<Asteroid>();
		ArrayList<Asteroid> asteroidsToAdd = new ArrayList<Asteroid>();
		
		for(Asteroid asteroid : asteroids)
		{
			boolean hit = false;
			Bullets bulletToDelete = null;
			
			for(Bullets bullet : bullets) {
				hit = asteroid.checkForCollision(bullet);
				
				if(hit) {
					bulletToDelete = bullet;
					
					asteroidCollision(asteroid, asteroidsToAdd, asteroidsToDelete);
				}
				
				if(hit) {
					if(bullet.playerOrigin == 1)
						playerOneShip.score += 5;
					else
						playerTwoShip.score += 5;
					
					break;
				}			
			}
				
			//if a hit was captured, no need to check for collisions with anything else
			if(hit) {
				bullets.remove(bulletToDelete);
				continue;
			}
			
			hit = asteroid.checkForCollision(playerOneShip);
			if(hit) {
				asteroidCollision(asteroid, asteroidsToAdd, asteroidsToDelete);
				playerOneGameOver = playerOneShip.died(screenWidth/2, screenHeight/2);
				
				hit = false;
			}
			
			if(isMultiplayer) {
				hit = asteroid.checkForCollision(playerTwoShip);
				
				if(hit) {
					asteroidCollision(asteroid, asteroidsToAdd, asteroidsToDelete);
					playerTwoGameOver = playerTwoShip.died(screenWidth/2, screenHeight/2);
				}
			}
		}
		
		asteroids.removeAll(asteroidsToDelete);
		asteroids.addAll(asteroidsToAdd);
		
		asteroidsToAdd.clear();
		asteroidsToDelete.clear();
	}

	private void asteroidCollision(Asteroid asteroid, ArrayList<Asteroid> asteroidsToAdd, ArrayList<Asteroid> asteroidsToDelete) {
		if(asteroid.getAstLevel() == AsteroidLevel.BIG) {						
			asteroidsToDelete.add(asteroid);
			for(int i = 0; i < 3; i++) {
				asteroidsToAdd.add(new Asteroid(asteroid, level));
			}
		}
		else if(asteroid.getAstLevel() == AsteroidLevel.SMALL) {
			asteroidsToDelete.add(asteroid);
		}
	}

	private void goToNextLevel() {
		paused = true;
		level++;
		
		bullets.clear();
		
		//Create asteroids (level 1 has 3, 2 has 4, etc)
		for(int i = 0; i < Math.min(level + 2, 20); i++)
			asteroids.add(new Asteroid(screenWidth, screenHeight, level, AsteroidLevel.BIG));
		
		if(isMultiplayer){
			playerOneShip = createOrUpdateShip(playerOneShip, 3*screenWidth/4, screenHeight/2, 0, false);
			playerTwoShip = createOrUpdateShip(playerTwoShip, screenWidth/4, screenHeight/2, Math.PI, true);
		}
		else
			playerOneShip = createOrUpdateShip(playerOneShip, screenWidth/2, screenHeight/2, 0, false);
		
		if(level > 1) {
			if(!playerOneShip.isDead)
				playerOneShip.score += 100;
			if(isMultiplayer && !playerTwoShip.isDead)
				playerTwoShip.score += 100;
		}
	}

	private Ship createOrUpdateShip(Ship ship, int x, int y, double angle, boolean isSecondPlayer) {
		if(ship == null)
			ship = new Ship(x, y, angle, isSecondPlayer);
		else if(!ship.isDead){
			ship.nextLevel(x, y, angle);
		}
		
		return ship;
	}

	private void handleKeyboardInputs() {
		ArrayList<Bullets> bulletsToDelete = new ArrayList<Bullets>();
		
		for(Bullets b : bullets)
		{
			if(b.distanceFromShootLocation > screenWidth)
			{
				bulletsToDelete.add(b);
				continue;
			}
			b.standardMove(screenWidth, screenHeight);
		}
		
		bullets.removeAll(bulletsToDelete);
		
		for(Asteroid a : asteroids)
		{
			a.standardMove(screenWidth, screenHeight);
		}
		
		if(paused)
			return;
		
		if(playerOneShip.upPress)
			playerOneShip.accelerate();
		if(playerOneShip.turnPress)
			playerOneShip.turn();
		if(playerOneShip.shotPress)
		{
			long currentTime = System.currentTimeMillis();
			
			if(currentTime > lastShotTime1 + 200)
			{
				playerOneShip.setShotCountOrReset(true); //reset shot count
			} 
			if(playerOneShip.getShotCount() < 4)
			{
				bullets.add(new Bullets(playerOneShip));
				lastShotTime1 = System.currentTimeMillis();
			}

			playerOneShip.shotPress = false;			
		}
		playerOneShip.standardMove(screenWidth, screenHeight);
		
		if(isMultiplayer) {
			if(playerTwoShip.upPress)
				playerTwoShip.accelerate();
			if(playerTwoShip.turnPress)
				playerTwoShip.turn();
			if(playerTwoShip.shotPress)
			{
				long currentTime = System.currentTimeMillis();
				
				if(currentTime > lastShotTime2 + 200)
				{
					playerTwoShip.setShotCountOrReset(true); //reset shot count
				} 
				if(playerTwoShip.getShotCount() < 4)
				{
					bullets.add(new Bullets(playerTwoShip));
					lastShotTime2 = System.currentTimeMillis();
				}

				playerTwoShip.shotPress = false;			
			}
			playerTwoShip.standardMove(screenWidth, screenHeight);	
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
				if(press)
					playerOneShip.updateAcceleration();
				else
					playerOneShip.stopAcceleration();
				break;
			case(KeyEvent.VK_LEFT):
				if(press)
					playerOneShip.updateAngle(Ship.AngleMultiplier.LEFT);
				else
					playerOneShip.stopTurning();
				break;
			case(KeyEvent.VK_RIGHT):
				if(press)
					playerOneShip.updateAngle(Ship.AngleMultiplier.RIGHT);
				else
					playerOneShip.stopTurning();
				break;
			case(KeyEvent.VK_EQUALS): //Firing
				if(press)
					playerOneShip.shoot();
				else
					playerOneShip.stopShooting();
				break;
			
			//Player 2 movements
			case(KeyEvent.VK_W):
				if(press)
					playerTwoShip.updateAcceleration();
				else
					playerTwoShip.stopAcceleration();
				break;
			case(KeyEvent.VK_A):
				if(press)
					playerTwoShip.updateAngle(Ship.AngleMultiplier.LEFT);
				else
					playerTwoShip.stopTurning();
				break;
			case(KeyEvent.VK_D):
				if(press)
					playerTwoShip.updateAngle(Ship.AngleMultiplier.RIGHT);
				else
					playerTwoShip.stopTurning();
				break;
			case(KeyEvent.VK_SPACE):
				if(press)
					playerTwoShip.shoot();
				else
					playerTwoShip.stopShooting();
				break;
			
			case(KeyEvent.VK_ENTER): //Start game after pause
				if(press)
					paused = !paused;
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
						
		JFrame frame = new JFrame("Asteroids");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(asteroids);
		frame.setSize(screenWidth, screenHeight);
		frame.setVisible(true);
		
		asteroids.spinNewThreadAndSetupGraphics();
		asteroids.start();
	}

	private void spinNewThreadAndSetupGraphics() {
		image = createImage(screenWidth, screenHeight);
		graphics = image.getGraphics();
		thread = new Thread(this);
		thread.start();
		//goToNextLevel();
	}
}
