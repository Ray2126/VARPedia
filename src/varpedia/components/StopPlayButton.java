package varpedia.components;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

/**
 * A button that when pressed will play an audio and will then turn into a stop button.
 * When pressed again the audio will stop and this button will turn back into a play button.
 *
 */
public class StopPlayButton extends Button{
	
	private MediaPlayer mediaPlayer;
	private int width;
	private int height;
	
	/**
	 * Constructor with parameters for height and width of the button
	 * @param height	the height of the button
	 * @param width		the width of the button
	 */
	public StopPlayButton(int height, int width) {
		super();
		Styling.blueButton(this);
		this.width = width;
		this.height = height;
		playIcon();
	}
	
	/**
	 * Play a media and associate the media player to this button
	 * to stop and play the media
	 * @param audio		the media to play
	 */
	public void audioPlayed(Media audio) {
    	if(mediaPlayer != null) {
			if(mediaPlayer.getStatus() == Status.PLAYING) {
				mediaPlayer.stop();
				playIcon();
				
			} else {
				mediaPlayer= new MediaPlayer(audio);
				mediaPlayer.play();
				stopIcon();
			}
    	} else {
			mediaPlayer= new MediaPlayer(audio);
			mediaPlayer.play();
			stopIcon();
			mediaPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
				if(mediaPlayer.getStatus() == Status.PLAYING) {
					stopIcon();
				}
				else {
					playIcon();
				}
			});
    	}
    	mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				playIcon();
				mediaPlayer = null;
			}
		});
	}
			
	private void playIcon() {
		this.setGraphic(LoadIcon.loadIcon("play", width, height));
	}
	
	private void stopIcon() {
		this.setGraphic(LoadIcon.loadIcon("stop", width, height));
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
}
