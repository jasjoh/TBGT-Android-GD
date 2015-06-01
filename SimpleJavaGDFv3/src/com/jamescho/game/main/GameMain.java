package com.jamescho.game.main;

import javax.swing.JFrame;

public class GameMain {
	private static final String GAME_TITLE = "Java Game Development Framework (Chapter 4)";
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 450;
	public static Game sGame;

	public static void main(String[] args) {
		JFrame frame = new JFrame(GAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false); // Prevents manual resizing of window
		sGame = new Game(GAME_WIDTH, GAME_HEIGHT);
		frame.add(sGame); // add the sGame JPanel to the JFrame
		// note:  once .add() is successful, sGame.addNotify() method is called
		frame.pack(); // re-sizes the JFrame to fit the sGame JPanel
		frame.setVisible(true); // display it
		frame.setIconImage(Resources.iconimage); // This is the new line!
	}

}