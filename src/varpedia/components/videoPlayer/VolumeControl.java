package varpedia.components.videoPlayer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

public class VolumeControl extends HBox{
	
	private Button muteButton;
	private Slider volumeSlider;
	private MediaPlayer mediaPlayer;
	private double previousVolume;

	public VolumeControl() {
		muteButton = new Button();
		Styling.blueButton(muteButton);
		volumeUpIcon();
		muteButton.setMaxWidth(60);
		muteButton.setMinWidth(60);
		muteButton.setDisable(true);
		volumeSlider = new Slider(0,100,100);
		volumeSlider.setDisable(true);
		this.setAlignment(Pos.CENTER);
		
		this.getChildren().addAll(muteButton,volumeSlider);
	}
	
	public void videoPlayed(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		muteButton.setDisable(false);
		volumeSlider.setDisable(false);
		setUpMute();
		setUpSlider();
	}

	private void setUpMute() {
		muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(mediaPlayer.getVolume() == 0){
					volumeOffIcon();
					if(previousVolume == -1) {
						mediaPlayer.setVolume(0);
					}
					else {
						mediaPlayer.setVolume(previousVolume);
						previousVolume = -1;
						volumeUpIcon();
					}
				}
				else {
					previousVolume = mediaPlayer.getVolume();
					mediaPlayer.setVolume(0);
					volumeOffIcon();
				}
				
				
			}
		});
		
	} 
	
	private void setUpSlider() {
		volumeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				mediaPlayer.setVolume(volumeSlider.getValue()/100);
			}
		});

		mediaPlayer.volumeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				volumeSlider.setValue(mediaPlayer.getVolume()*100);
			}
			
		});
		
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if(volumeSlider.getValue() == 0) {
					volumeOffIcon();
				}
				else {
					volumeUpIcon();
				}
			}
		});
	}
	
	private void volumeUpIcon() {
		muteButton.setGraphic(LoadIcon.loadIcon("volumeUp", 30, 30));
	}
	
	private void volumeOffIcon() {
		muteButton.setGraphic(LoadIcon.loadIcon("volumeOff", 30, 30));
	}
}
