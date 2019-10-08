package varpedia.videoPlayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class PauseButton extends Button{

	public PauseButton() {
		super("Play");
		this.setPrefWidth(60);
		this.setDisable(true);

	}
	
	public void videoPlayed(MediaPlayer mp) {
		this.setDisable(false);
		this.setText("Pause");
		
		//Add functionality for play/pause
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(mp.getStatus() == Status.PLAYING) {
					mp.pause();
					setText("Play");
				}
				else {
					mp.play();
					setText("Pause");
				}
			}
		});
	}
}
