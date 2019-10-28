package varpedia.components.videoPlayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

/**
 * The play/pause button for the video player
 *
 */
public class PauseButton extends Button{

	private int width;
	private int height;
	
	/**
	 * The constructor with parameters for the height and width of this button
	 * @param width		the width of this button
	 * @param height	the height of this button
	 */
	public PauseButton(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		Styling.blueButton(this);
		
		playImage();
		
		this.setMaxHeight(this.height);
		this.setMaxWidth(this.width);
		this.setDisable(true);
	}
	
	/**
	 * Add functionality to this button and enable as video has been played
	 * @param mediaPlayer  the media player binded to this button
	 */
	public void videoPlayed(MediaPlayer mediaPlayer) {
		this.setDisable(false);

		mediaPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
			if(mediaPlayer.getStatus() == Status.PLAYING) {
				pauseImage();
			}
			else {
				playImage();
			}
		});
		
		//Add functionality for play/pause
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(mediaPlayer.getStatus() == Status.PLAYING) {
					mediaPlayer.pause();
				}
				else {
					mediaPlayer.play();
				}
			}
		});
	}

	private void playImage() {
		this.setGraphic(LoadIcon.loadIcon("play", width, height));
	}
	
	private void pauseImage() {
		this.setGraphic(LoadIcon.loadIcon("pause", width, height));
	}
}
