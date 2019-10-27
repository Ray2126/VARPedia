package varpedia.components;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

public class StopPlayButton extends Button{
	
	private MediaPlayer mediaPlayer;
	private int width;
	private int height;
	
	public StopPlayButton(int height, int width) {
		super();
		Styling.blueButton(this);
		this.width = width;
		this.height = height;
		playIcon();
	}
	
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
