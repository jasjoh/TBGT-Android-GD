package com.jamescho.game.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Resources {

	public static BufferedImage welcome, iconimage;

	public static void load() {
		// called by LoadState object when its init() method is called via Game.addNotify()
		welcome = loadImage("welcome.png");
		iconimage = loadImage("iconimage.png");
	}

	private static AudioClip loadSound(String filename) {
		URL fileURL = Resources.class.getResource("/resources/" + filename);
		return Applet.newAudioClip(fileURL);
	}

	private static BufferedImage loadImage(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(Resources.class.getResourceAsStream("/resources/" + filename));
		} catch (IOException e) {
			System.out.println("Error while reading: " + filename);
			e.printStackTrace();
		}
		return img;
	}
}