package varpedia.components.videoPlayer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;

public class VolumeControl extends HBox{
	
	private Button _muteButton;
	private Slider _volumeSlider;
	private MediaPlayer _mediaPlayer;
	private double _previousVolume;

	public VolumeControl() {
		_muteButton = new Button("Mute");
		_muteButton.setMaxWidth(60);
		_muteButton.setMinWidth(60);
		_muteButton.setDisable(true);
		_volumeSlider = new Slider(0,100,100);
		_volumeSlider.setDisable(true);
		
		this.getChildren().addAll(_muteButton,_volumeSlider);
	}
	
	public void videoPlayed(MediaPlayer mediaPlayer) {
		_mediaPlayer = mediaPlayer;
		_muteButton.setDisable(false);
		_volumeSlider.setDisable(false);
		setUpMute();
		setUpSlider();
	}

	private void setUpMute() {
		_muteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(_mediaPlayer.getVolume() == 0){
					_muteButton.setText("Muted");
					if(_previousVolume == -1) {
						_mediaPlayer.setVolume(0);
					}
					else {
						_mediaPlayer.setVolume(_previousVolume);
						_previousVolume = -1;
						_muteButton.setText("Mute");
					}
				}
				else {
					_previousVolume = _mediaPlayer.getVolume();
					_mediaPlayer.setVolume(0);
					_muteButton.setText("Muted");
				}
				
				
			}
		});
		
	} 
	
	private void setUpSlider() {
		_volumeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				_mediaPlayer.setVolume(_volumeSlider.getValue()/100);
			}
		});

		_mediaPlayer.volumeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				_volumeSlider.setValue(_mediaPlayer.getVolume()*100);
			}
			
		});
		
		_volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if(_volumeSlider.getValue() == 0) {
					_muteButton.setText("Muted");
				}
				else {
					_muteButton.setText("Mute");
				}
			}
		});
	}
}
