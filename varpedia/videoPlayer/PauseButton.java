package varpedia.videoPlayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * The play/pause button for the video player
 *
 */
public class PauseButton extends Button{

	public PauseButton() {
		super("Play");
		this.setPrefWidth(60);
		this.setDisable(true);
	}
	
	/**
	 * Enable, change text and add functionality as video has been played
	 * @param mediaPlayer  the media player binded to this button
	 */
	public void videoPlayed(MediaPlayer mediaPlayer) {
		this.setDisable(false);
		this.setText("Pause");
		
		//Add functionality for play/pause
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(mediaPlayer.getStatus() == Status.PLAYING) {
					mediaPlayer.pause();
					setText("Play");
				}
				else {
					mediaPlayer.play();
					setText("Pause");
				}
			}
		});
	}
}
