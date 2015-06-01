package com.jamescho.framework.animation;

import java.awt.Image;


// we will use Frames within our Animation class
public class Frame {
	private Image image; // the image associated with this Frame
	private double duration; // the duration that the image should be displayed
	
	public Frame(Image image, double duration) {
		this.image = image;
		this.duration = duration;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public Image getImage() {
		return image;
	}
}
