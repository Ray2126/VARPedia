package varpedia.videoPlayer;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

public class TimeSlider extends Slider{

	public TimeSlider() {
		super(0,100,0);
		this.setDisable(true);
	}
	
	public void videoPlayed(MediaPlayer mp) {
		this.setDisable(false);
		
		//Makes the slider move as the video plays
		mp.currentTimeProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						setValue((mp.getCurrentTime().toMillis() / mp.getTotalDuration().toMillis()) * 100);
					}
				});
			}
		});
		
		//Functionality to jump to certain parts of video by dragging slider
		valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (isPressed()) {
					mp.seek(mp.getMedia().getDuration().multiply(getValue()/100));
				} 
			}
		});
	}
}
