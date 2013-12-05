package asteroids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public class AsteroidsGame extends JFrame implements Runnable, KeyListener{
	Thread thread;
	static int screenWidth;
	static int screenHeight;
	
	public AsteroidsGame() {
		thread = new Thread();
		thread.start();
		super.setTitle("Asteroids");
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);
	}
	public void run() {
		for (;;){
			repaint();
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
				break;
			case(KeyEvent.VK_LEFT):
				break;
			case(KeyEvent.VK_RIGHT):
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
		AsteroidsGame asteroids = new AsteroidsGame();
		asteroids.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		asteroids.setSize(screenWidth, screenHeight);
		asteroids.setVisible(true);
	}
}
