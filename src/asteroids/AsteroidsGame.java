package asteroids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class AsteroidsGame extends JFrame implements Runnable, KeyListener{
	Thread thread;
	static int screenWidth;
	static int screenHeight;
	
	public AsteroidsGame() {
		thread = new Thread();
		thread.start();
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
	public static void main(String[] args) {
		AsteroidsGame asteroids = new AsteroidsGame();
		asteroids.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width - 50;
		screenHeight = screenSize.height - 50;
		asteroids.setSize(screenWidth, screenHeight);
		asteroids.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
