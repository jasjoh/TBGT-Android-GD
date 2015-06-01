package com.jamescho.framework.animation;

import java.awt.Graphics;

public class Animation {
	private Frame[] frames;
	private double[] frameEndTimes;
	private int currentFrameIndex = 0;
	
	private double totalDuration = 0;
	private double currentTime = 0;
	
	public Animation(Frame... frames) {
		this.frames = frames; // grab all the frames passed in and put them in our array
		frameEndTimes = new double[frames.length];
		
		for (int i = 0; i < frames.length; i++) {
			Frame f = frames[i]; // create a temporary frame to hold the frame @ index 'i'
				// why do we need the above, can't we just do "totalDuration += frames[i].getDuration();" ??
			totalDuration += f.getDuration(); // add the duration to totalDuration
			frameEndTimes[i] = totalDuration; // define the parallel end time for each frame
		}
	}
	
	public synchronized void update(float increment) {
		// this method finds the right frame for us to be on
		currentTime += increment;
		
		// if the animation loop is finished, start it over again
		if (currentTime > totalDuration) {
			wrapAnimation();
		}
		
		// advance to the correct frame
		while (currentTime > frameEndTimes[currentFrameIndex]) {
			currentFrameIndex++;
		}
	}
	
	private synchronized void wrapAnimation() {
		// the animation loop finished so let's reset it
		currentFrameIndex = 0;
		currentTime %= totalDuration; // set the current time to the time that has elapsed since the last frame was supposed to finish being displayed
			// take the current time (should be greater than the total time the loop should take)
			// divide the current time by the total time the loop should take and find the remainder
			// set the value of the current time to the remainder
			// for example:  animation loop should take 1000ms and current time is 1010ms, current time becomes 10ms
			// the first frame doesn't get as long to display but our animation remains smooth (i think??)
	}
	
	public synchronized void render(Graphics g, int x, int y) {
		g.drawImage(frames[currentFrameIndex].getImage(), x, y, null);
	}
	
	public synchronized void render (Graphics g, int x, int y, int width, int height) {
		g.drawImage(frames[currentFrameIndex].getImage(), x, y, width, height, null);
	}
}
