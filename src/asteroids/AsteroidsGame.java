package asteroids;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import asteroids.Asteroid.AsteroidLevel;

@SuppressWarnings("serial")
public class AsteroidsGame extends Applet implements Runnable, KeyListener,
		ActionListener {
	Thread thread;
	static int screenWidth;
	static int screenHeight;
	private List<Bullets> bullets;
	private List<Asteroid> asteroids;
	private List<Bullets> evilBullets;
	private Ship playerOneShip;
	private Ship playerTwoShip;

	private boolean isMultiplayer;
	private boolean isGravObjActive;
	private boolean isGravObjVisible;
	private GravitationalObject gravObj;

	private long lastShotTime1;
	private long lastShotTime2;
	private long lastShotTimeAlien;
	private long lastShotTimeRogue;

	private Image image;
	private Graphics graphics;
	private int level;

	private boolean playerOneGameOver = false;
	private boolean playerTwoGameOver = false;

	private boolean paused = true;

	private RogueShip rogue;
	private boolean rogueDestroyed = false;

	private AlienShip alien;
	private boolean alienDestroyed = false;
	private boolean gamePaused = false;

	private boolean displayPauseMenu = false;
	private boolean openDialog = true;
	
	private JDialog dialog;
	
	private String[] buttonNames = { "Continue",
			"Toggle Gravitational Object: ", "Gravitational Object Visible: ",
			"Free Play: ", "Number Of Astroids: ", "Reset High Scores",
			"Save Game", "Load Game", "Starting Level: ", "Multiplayer: ",
			"Quit" };

	private JFrame frame;

	private Object[] quitOptions = {"Yes", "No"};
	
	
	public AsteroidsGame() {
		super();
		addKeyListener(this);

		level = 0;
		bullets = new ArrayList<Bullets>();
		asteroids = new ArrayList<Asteroid>();
		evilBullets = new ArrayList<Bullets>();

		isMultiplayer = true;
		isGravObjActive = true;
		isGravObjVisible = true;
	}

	public void paint(Graphics gGeneric) {
		Graphics2D g = (Graphics2D) graphics;
		RenderingHints render = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setRenderingHints(render);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		for (Bullets b : bullets)
			b.draw(g);

		for (Bullets b : evilBullets)
			b.draw(g);

		for (Asteroid a : asteroids)
			a.draw(g);

		if (gravObj != null && isGravObjActive && isGravObjVisible)
			gravObj.draw(g);

		Font font = new Font("Veranda", Font.PLAIN, 25);

		if (paused)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.WHITE);

		g.setFont(font);
		g.drawString("Level : " + level, screenWidth / 2, screenHeight - 50);

		// Draw the players ship and score/lives
		if (playerOneShip != null) {
			if (!paused)
				playerOneShip.color = Color.CYAN;
			else
				playerOneShip.color = Color.DARK_GRAY;

			g.setColor(playerOneShip.color);
			g.drawString("Player 1: " + "Lives : " + playerOneShip.lives
					+ " Score : " + playerOneShip.score, 0, screenHeight - 50);

			if (!playerOneGameOver)
				playerOneShip.draw(g);
		}

		if (isMultiplayer && playerTwoShip != null) {
			if (!paused)
				playerTwoShip.color = Color.MAGENTA;
			else
				playerTwoShip.color = Color.DARK_GRAY;

			g.setColor(playerTwoShip.color);

			g.drawString("Player 2: " + "Lives : " + playerTwoShip.lives
					+ " Score : " + playerTwoShip.score, 0, screenHeight - 75);

			if (!playerTwoGameOver)
				playerTwoShip.draw(g);
		}

		if (rogue != null && !rogueDestroyed)
			rogue.draw(g);

		if (alien != null && !alienDestroyed)
			alien.draw(g);

		gGeneric.drawImage(image, 0, 0, this);
	}

	public void run() {
		for (;;) {

			if (!displayPauseMenu) {
				openDialog = true;

				if (asteroids.isEmpty()) {
					goToNextLevel();
				}

				handleKeyboardInputs();

				if (!paused) {
					checkObjectsForCollision();
				}
				repaint();
			} else {
				if (openDialog) {
					dialog = new JDialog();
					dialog.setTitle("The Store");
					dialog.setSize(215, 315);
					dialog.setLocation(screenWidth / 2 - 150, screenHeight / 2 - 150);

					Container pane = dialog.getContentPane();
					pane.setLayout(null);
					int count = 0;
					for (String s : buttonNames) {
						JButton b = new JButton(s);
						b.setBounds(0, count * 25, 200, 25);
						b.addActionListener(this);
						b.setVisible(true);
						pane.add(b);
						count++;
					}

					dialog.setVisible(true);
					openDialog = false;
				}
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
		boolean hit = false;
		ArrayList<Bullets> bulletToDelete = new ArrayList<Bullets>();

		for (Asteroid asteroid : asteroids) {
			hit = false;

			for (Bullets bullet : bullets) {
				hit = asteroid.checkForCollision(bullet);

				if (hit) {
					bulletToDelete.add(bullet);

					asteroidCollision(asteroid, asteroidsToAdd,
							asteroidsToDelete);
				}

				if (hit) {
					if (bullet.playerOrigin == 1)
						playerOneShip.score += 5;
					else
						playerTwoShip.score += 5;

					break;
				}
			}

			// if a hit was captured, no need to check for collisions with
			// anything else
			if (hit) {
				bullets.removeAll(bulletToDelete);
				bulletToDelete.clear();
				continue;
			}

			hit = asteroid.checkForCollision(playerOneShip);
			if (hit) {
				asteroidCollision(asteroid, asteroidsToAdd, asteroidsToDelete);
				playerOneGameOver = playerOneShip.died(screenWidth / 2,
						screenHeight / 2);

				hit = false;
			}

			if (isMultiplayer) {
				hit = asteroid.checkForCollision(playerTwoShip);

				if (hit) {
					asteroidCollision(asteroid, asteroidsToAdd,
							asteroidsToDelete);
					playerTwoGameOver = playerTwoShip.died(screenWidth / 2,
							screenHeight / 2);
				}
			}
		}

		asteroids.removeAll(asteroidsToDelete);
		asteroids.addAll(asteroidsToAdd);

		asteroidsToAdd.clear();
		asteroidsToDelete.clear();

		// Rogue
		hit = false;
		bulletToDelete.clear();

		if (rogue != null && !rogueDestroyed) {
			for (Bullets bullet : bullets) {
				hit = rogue.checkForCollision(bullet);

				if (hit) {
					bulletToDelete.add(bullet);

					rogueDestroyed = true;

					if (bullet.playerOrigin == 1)
						playerOneShip.score += 100;
					else
						playerTwoShip.score += 100;

					break;
				}
			}

			// if a hit was captured, no need to check for collisions with
			// anything else
			if (hit) {
				bullets.removeAll(bulletToDelete);
				bulletToDelete.clear();
				hit = false;
			}

			hit = rogue.checkForCollision(playerOneShip);
			if (hit) {

				playerOneGameOver = playerOneShip.died(screenWidth / 2,
						screenHeight / 2);
				rogueDestroyed = true;
				hit = false;
			}

			if (isMultiplayer) {
				hit = rogue.checkForCollision(playerTwoShip);

				if (hit) {
					playerTwoGameOver = playerTwoShip.died(screenWidth / 2,
							screenHeight / 2);
					rogueDestroyed = true;
					hit = false;
				}
			}
		}

		// Alien Ship
		if (alien != null && !alienDestroyed) {
			for (Bullets b : bullets) {
				hit = alien.checkForCollision(b);

				if (hit) {
					bulletToDelete.add(b);

					alien.livesLeft--;
					if (alien.livesLeft <= 0) {
						alienDestroyed = true;

						if (b.playerOrigin == 1)
							playerOneShip.score += 100;
						else
							playerTwoShip.score += 100;
					}
					break;
				}
			}

			if (hit) {
				bullets.removeAll(bulletToDelete);
				bulletToDelete.clear();
				hit = false;
			}

			hit = alien.checkForCollision(playerOneShip);
			if (hit) {

				playerOneGameOver = playerOneShip.died(screenWidth / 2,
						screenHeight / 2);
				alien.livesLeft--;
				if (alien.livesLeft <= 0)
					alienDestroyed = true;

				hit = false;
			}

			if (isMultiplayer) {
				hit = alien.checkForCollision(playerTwoShip);

				if (hit) {
					playerTwoGameOver = playerTwoShip.died(screenWidth / 2,
							screenHeight / 2);

					alien.livesLeft--;
					if (alien.livesLeft <= 0)
						alienDestroyed = true;
					hit = false;
				}
			}
		}

		// Ships with evil bullets
		for (Bullets b : evilBullets) {
			hit = playerOneShip.checkForCollision(b);
			if (hit && !playerOneShip.isDead) {
				bulletToDelete.add(b);
				playerOneGameOver = playerOneShip.died(screenWidth / 2,
						screenHeight / 2);
				continue;
			}

			if (isMultiplayer && !playerTwoShip.isDead) {
				hit = playerTwoShip.checkForCollision(b);
				if (hit) {
					bulletToDelete.add(b);
					playerTwoGameOver = playerTwoShip.died(screenWidth / 2,
							screenHeight / 2);
				}
			}
		}

		bullets.removeAll(bulletToDelete);
	}

	private void asteroidCollision(Asteroid asteroid,
			ArrayList<Asteroid> asteroidsToAdd,
			ArrayList<Asteroid> asteroidsToDelete) {
		if (asteroid.getAstLevel() == AsteroidLevel.BIG) {
			asteroidsToDelete.add(asteroid);
			for (int i = 0; i < 3; i++) {
				asteroidsToAdd.add(new Asteroid(asteroid, level));
			}
		} else if (asteroid.getAstLevel() == AsteroidLevel.SMALL) {
			asteroidsToDelete.add(asteroid);
		}
	}

	private void goToNextLevel() {
		paused = true;
		level++;

		bullets.clear();

		if (isGravObjActive)
			gravObj = new GravitationalObject(screenWidth, screenHeight);

		if (level % 2 == 0) {
			rogueDestroyed = false;
			rogue = new RogueShip(screenWidth, screenHeight, level);
		}

		if (level % 3 == 0) {
			alienDestroyed = false;
			alien = new AlienShip(screenWidth, screenHeight, level);
		}
		// Create asteroids (level 1 has 3, 2 has 4, etc)
		for (int i = 0; i < Math.min(level + 2, 20); i++)
			asteroids.add(new Asteroid(screenWidth, screenHeight, level,
					AsteroidLevel.BIG));

		if (isMultiplayer) {
			playerOneShip = createOrUpdateShip(playerOneShip,
					3 * screenWidth / 4, screenHeight / 2, 0, false);
			playerTwoShip = createOrUpdateShip(playerTwoShip, screenWidth / 4,
					screenHeight / 2, Math.PI, true);
		} else
			playerOneShip = createOrUpdateShip(playerOneShip, screenWidth / 2,
					screenHeight / 2, 0, false);

		if (level > 1) {
			if (!playerOneShip.isDead)
				playerOneShip.score += 100;
			if (isMultiplayer && !playerTwoShip.isDead)
				playerTwoShip.score += 100;
		}
	}

	private Ship createOrUpdateShip(Ship ship, int x, int y, double angle,
			boolean isSecondPlayer) {
		if (ship == null)
			ship = new Ship(x, y, angle, isSecondPlayer);
		else if (!ship.isDead) {
			ship.nextLevel(x, y, angle);
		}

		return ship;
	}

	private void handleKeyboardInputs() {
		ArrayList<Bullets> bulletsToDelete = new ArrayList<Bullets>();

		ArrayList<Bullets> allBullets = new ArrayList<Bullets>();
		allBullets.addAll(bullets);
		allBullets.addAll(evilBullets);

		for (Bullets b : allBullets) {
			if (b.distanceFromShootLocation > screenWidth) {
				bulletsToDelete.add(b);
				continue;
			}
			b.standardMove(screenWidth, screenHeight);
		}

		bullets.removeAll(bulletsToDelete);
		evilBullets.removeAll(bulletsToDelete);

		for (Asteroid a : asteroids) {
			a.standardMove(screenWidth, screenHeight);
		}

		if (rogue != null && !rogueDestroyed) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastShotTimeAlien > (200 * Math.max(-1 * level
					+ 25, 0))) {
				evilBullets.add(new Bullets(rogue));
				lastShotTimeRogue = System.currentTimeMillis();
			}

			rogue.standardMove(screenWidth, screenHeight);
		}

		if (alien != null && !alienDestroyed) {
			alien.standardMove(screenWidth, screenHeight);

			long currentTime = System.currentTimeMillis();

			if (currentTime > lastShotTimeAlien + 200) {
				alien.setShotCountOrReset(true); // reset shot count
			}
			if (alien.shotCount < 4
					&& currentTime - lastShotTimeAlien > (200 * Math.max(-1
							* level + 25, 0))) {
				if (!isMultiplayer)
					evilBullets.add(new Bullets(alien, playerOneShip));
				else {
					// figure out which is closer
					Point2D oneMiddle = playerOneShip.getMiddle();
					Point2D twoMiddle = playerTwoShip.getMiddle();
					Point2D alienMiddle = alien.getMiddle();

					if ((alienMiddle.distance(oneMiddle) > alienMiddle
							.distance(twoMiddle) && !playerTwoShip.isDead)
							|| playerOneShip.isDead)
						evilBullets.add(new Bullets(alien, playerTwoShip));
					else
						evilBullets.add(new Bullets(alien, playerOneShip));
				}

				lastShotTimeAlien = System.currentTimeMillis();
			}
		}

		if (paused)
			return;

		if (playerOneShip.upPress)
			playerOneShip.accelerate();
		if (playerOneShip.turnPress)
			playerOneShip.turn();
		if (isGravObjActive)
			playerOneShip.adjustForGrav(screenWidth, screenHeight);
		if (playerOneShip.shotPress) {
			long currentTime = System.currentTimeMillis();

			if (currentTime > lastShotTime1 + 200) {
				playerOneShip.setShotCountOrReset(true); // reset shot count
			}
			if (playerOneShip.getShotCount() < 4) {
				bullets.add(new Bullets(playerOneShip));
				lastShotTime1 = System.currentTimeMillis();
			}

			playerOneShip.shotPress = false;
		}
		playerOneShip.standardMove(screenWidth, screenHeight);

		if (isMultiplayer) {
			if (playerTwoShip.upPress)
				playerTwoShip.accelerate();
			if (playerTwoShip.turnPress)
				playerTwoShip.turn();
			if (isGravObjActive)
				playerTwoShip.adjustForGrav(screenWidth, screenHeight);
			if (playerTwoShip.shotPress) {
				long currentTime = System.currentTimeMillis();

				if (currentTime > lastShotTime2 + 200) {
					playerTwoShip.setShotCountOrReset(true); // reset shot count
				}
				if (playerTwoShip.getShotCount() < 4) {
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
		// Nothing needed here
	}

	private void KeyPressRelease(KeyEvent event, boolean press) {
		int keyCode = event.getKeyCode();

		switch (keyCode) {
		// Player 1 movements
		case (KeyEvent.VK_UP):
			if (press)
				playerOneShip.updateAcceleration();
			else
				playerOneShip.stopAcceleration();
			break;
		case (KeyEvent.VK_LEFT):
			if (press)
				playerOneShip.updateAngle(Ship.AngleMultiplier.LEFT);
			else
				playerOneShip.stopTurning();
			break;
		case (KeyEvent.VK_RIGHT):
			if (press)
				playerOneShip.updateAngle(Ship.AngleMultiplier.RIGHT);
			else
				playerOneShip.stopTurning();
			break;
		case (KeyEvent.VK_EQUALS): // Firing
			if (press)
				playerOneShip.shoot();
			else
				playerOneShip.stopShooting();
			break;

		// Player 2 movements
		case (KeyEvent.VK_W):
			if (press)
				playerTwoShip.updateAcceleration();
			else
				playerTwoShip.stopAcceleration();
			break;
		case (KeyEvent.VK_A):
			if (press)
				playerTwoShip.updateAngle(Ship.AngleMultiplier.LEFT);
			else
				playerTwoShip.stopTurning();
			break;
		case (KeyEvent.VK_D):
			if (press)
				playerTwoShip.updateAngle(Ship.AngleMultiplier.RIGHT);
			else
				playerTwoShip.stopTurning();
			break;
		case (KeyEvent.VK_SPACE):
			if (press)
				playerTwoShip.shoot();
			else
				playerTwoShip.stopShooting();
			break;

		case (KeyEvent.VK_ENTER): // Start game after pause
			if (press)
				paused = !paused;
			break;
		case (KeyEvent.VK_ESCAPE): // Bring up pause menu
			displayPauseMenu = true;
			break;
		case (KeyEvent.VK_END): // Exit
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
		// frame.add(asteroids);
		frame.setSize(screenWidth, screenHeight);
		frame.setVisible(true);

		asteroids.frame = frame;

		asteroids.spinNewThreadAndSetupGraphics();
		asteroids.start();
	}

	private void spinNewThreadAndSetupGraphics() {
		frame.add(this);

		image = createImage(screenWidth, screenHeight);
		graphics = image.getGraphics();
		thread = new Thread(this);
		thread.start();
		// goToNextLevel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();

		String buttonPressed = button.getText();

		if (buttonPressed == buttonNames[0]) { // Continue
			
			displayPauseMenu = !displayPauseMenu;
			openDialog = !openDialog;
			
			// close dialog
			dialog.setVisible(false);
			dialog.dispose();
			
		} else if (buttonPressed == buttonNames[1]) { // Toggle Gravitational Object
			
			if((gravObj != null) && isGravObjActive) {
				gravObj = null;
				isGravObjActive = !isGravObjActive;
			} else if ((gravObj == null && !isGravObjActive)) {
				gravObj = new GravitationalObject(screenWidth, screenHeight);
				isGravObjActive = !isGravObjActive;
			}
			
		} else if (buttonPressed == buttonNames[2]) { // Gravitational Object Visible
			
			if((gravObj != null) && isGravObjActive && isGravObjVisible) {
				
				isGravObjVisible = !isGravObjVisible;
			}
			else if((gravObj != null) && isGravObjActive && !isGravObjVisible) {
				isGravObjVisible = !isGravObjVisible;
			}
		} else if (buttonPressed == buttonNames[3]) { // Free Play

		} else if (buttonPressed == buttonNames[4]) { // Number of Asteroids

		} else if (buttonPressed == buttonNames[5]) { // Reset High Score

		} else if (buttonPressed == buttonNames[6]) { // Save Game

		} else if (buttonPressed == buttonNames[7]) { // Load Game

		} else if (buttonPressed == buttonNames[8]) { // Starting Level
		
		} else if (buttonPressed == buttonNames[9]) { // Multiplayer
		
		} else if (buttonPressed == buttonNames[10]) { // Quit
			//Ask for confirmation
			JDialog qDialog;
			int n = JOptionPane.showOptionDialog(qDialog = new JDialog(), "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, quitOptions, quitOptions[1]);
			if (n == JOptionPane.YES_OPTION) {
				
				//TODO: Need to add high score
				
				// close dialog
				dialog.setVisible(false);
				dialog.dispose();
				
				//close game
				frame.setVisible(false);
				frame.dispose();
				
			}else{
			}
			//qDialog.setVisible(true);
			

		}
	}
}