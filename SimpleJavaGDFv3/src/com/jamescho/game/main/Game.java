package com.jamescho.game.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.jamescho.framework.util.InputHandler;
import com.jamescho.game.state.LoadState;
import com.jamescho.game.state.State;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable {
	private int gameWidth;
	private int gameHeight;
	private Image gameImage;

	private Thread gameThread;
	private volatile boolean running;
	private volatile State currentState;

	private InputHandler inputHandler;

	public Game(int gameWidth, int gameHeight) {
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		setPreferredSize(new Dimension(gameWidth, gameHeight));
		setBackground(Color.BLACK);
		setFocusable(true);
		requestFocus();
	}

	public void setCurrentState(State newState) {
		// called to update current stage; pass in the state (e.g. LoadState)
		// called by addNotify() with 'new LoadState()' when loading initial resources
		System.gc(); // removed unused objects
		newState.init(); // call the init() method of the state
		currentState = newState; 
		inputHandler.setCurrentState(currentState); // update the inputHandler to reflect the current state in Game
	}

	@Override
	public void addNotify() {
		// addNotify is called automatically when our Game object has been successfully ...
		// ... added to the JFrame inside GameMain.  It is therefore a safe place to ...
		// ... start setting up graphics, game state, and user input
		super.addNotify(); // the superclass version of this method has other stuff we still want run
		initInput(); // have this JPanel start listening to key press / mouse click events
		setCurrentState(new LoadState()); // sets currentState to a LoadState object
		initGame(); // currentState is now set to LoadState(), time to launch the main game thread
	}

	private void initGame() {
		running = true;
		gameThread = new Thread(this, "Game Thread"); // indicate to create a new thread using this Game object
		gameThread.start(); // asking gameThread Thread (which is this Game object) to run it's run() method
	}

	@Override
	public void run() {
		// these vars should sum up to 17 (ms) on every iteration
		long updateRenderDurationMillis = 0; // measures time for update & render
		long sleepDurationMillis = 0;
		while (running) {
			long nanoTimeBeforeUpdateRender = System.nanoTime();	
			long lastRunDurationMillis = updateRenderDurationMillis + sleepDurationMillis;

			updateAndRender(lastRunDurationMillis); // update state and render the frame
			
			updateRenderDurationMillis = (System.nanoTime() - nanoTimeBeforeUpdateRender) / 1000000L;
			sleepDurationMillis = Math.max(2, 17 - updateRenderDurationMillis); // sleep at least 2 seconds
			// logic for sleeping a certain amount of time to maintain ~60 FPS (17ms per frame)
			try {
				Thread.sleep(sleepDurationMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// End game immediately when running becomes false.
		System.exit(0);
	}

	private void updateAndRender(long lastRunDurationMillis) {
		// call the update() method of the current state (e.g LoadState, MenuState, etc.)
		// for LoadState(), this updates the currentState to MenuState() using setCurrentState(new MenuState())
		currentState.update(lastRunDurationMillis / 1000f); // tell the update how much time has passed since the last call
		prepareGameImage(); // create an off-screen (buffered) image (or clear an existing one)
		currentState.render(gameImage.getGraphics()); // calls the render method of our current State() object
		renderGameImages(getGraphics()); // // get the Graphics object of our sGame JPanel and pass it to our render method
	}

	private void prepareGameImage() {
		// create our off-screen image or clear it if it's not clear
		if (gameImage == null) {
			gameImage = createImage(gameWidth, gameHeight);
		}
		Graphics g = gameImage.getGraphics();
		g.clearRect(0, 0, gameWidth, gameHeight);
	}

	public void exit() {
		running = false;
	}

	private void renderGameImages(Graphics g) {		
		if (gameImage == null) {
			g.drawImage(gameImage, 0, 0, null);
		}
		g.dispose();
	}

	private void initInput() {
		inputHandler = new InputHandler(); 
		addKeyListener(inputHandler); // call built in JPanel method to indicate inputHandler should be used to handle key input
		addMouseListener(inputHandler); // call built in JPanel method to indicate inputHandler should be used to handle mouse input
	}
}