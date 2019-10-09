package varpedia.videoPlayer;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

/**
 * The time slider in the video player
 *
 */
public class TimeSlider extends Slider{

	public TimeSlider() {
		super(0,100,0);
		this.setDisable(true);
	}
	
	/**
	 * Enable and add functionality to slider
	 * @param mediaPlayer   mediaPlayer this slider is binded to
	 */
	public void videoPlayed(MediaPlayer mediaPlayer) {
		this.setDisable(false);
		
		//Makes the slider move as the video plays
		mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						setValue((mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis()) * 100);
					}
				});
			}
		});
		
		//Functionality to jump to certain parts of video by dragging slider
		valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				if (isPressed()) {
					mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(getValue()/100));
				} 
			}
		});
	}
}
