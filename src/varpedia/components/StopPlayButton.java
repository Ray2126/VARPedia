package varpedia.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

public class StopPlayButton extends Button{
	
	private MediaPlayer _mediaPlayer;
	private int _width;
	private int _height;
	
	public StopPlayButton(int height, int width) {
		super();
		Styling.blueButton(this);
		_width = width;
		_height = height;
		playIcon();
	}
	
	public void audioPlayed(Media audio) {
    	if(_mediaPlayer != null) {
			if(_mediaPlayer.getStatus() == Status.PLAYING) {
				_mediaPlayer.stop();
				playIcon();
				
			} else {
				_mediaPlayer= new MediaPlayer(audio);
				_mediaPlayer.play();
				stopIcon();
			}
    	} else {
			_mediaPlayer= new MediaPlayer(audio);
			_mediaPlayer.play();
			stopIcon();
    	}
    	_mediaPlayer.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				playIcon();
				_mediaPlayer = null;
			}
		});
	}
			
	private void playIcon() {
		this.setGraphic(LoadIcon.loadIcon("play", _width, _height));
	}
	
	private void stopIcon() {
		this.setGraphic(LoadIcon.loadIcon("stop", _width, _height));
	}
	
	public MediaPlayer getMediaPlayer() {
		return _mediaPlayer;
	}
}
