package varpedia.videoPlayer;

import java.io.File;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import varpedia.Creation;

/**
 * Video player seen on the main screen and preview screen
 */
public class VideoPlayer extends VBox{

	private PauseButton _playPauseButton;
	private Button _skipForwardButton;
	private Button _skipBackwardButton;
	private Slider _volumeSlider;
	private TimeSlider _timeSlider;
	private VBox _mediaPlayerPane;
	private MediaPlayer _mediaPlayer;
	private MediaView _mediaView;
	
	
	public VideoPlayer() {
		_playPauseButton = new PauseButton();
		_skipForwardButton = new Button(">>|");
		_skipBackwardButton = new Button("|<<");
		_timeSlider = new TimeSlider();
		_volumeSlider = new Slider(0,100,100);
		
		//Disable buttons when video is not played yet
		_skipForwardButton.setDisable(true);
		_skipBackwardButton.setDisable(true);
		_volumeSlider.setDisable(true);
		
		_mediaPlayerPane = new VBox();
		
		//Set a black box as a placeholder for the video
		Rectangle r = new Rectangle(1000,500);
		_mediaPlayerPane.getChildren().addAll(r);
		
		//Set size of video player pane
		_mediaPlayerPane.setMinHeight(500);
		_mediaPlayerPane.setMaxHeight(500);
		_mediaPlayerPane.setPrefHeight(500);
		_mediaPlayerPane.setMinWidth(1000);
		_mediaPlayerPane.setMaxWidth(1000);
		_mediaPlayerPane.setPrefWidth(1000);
		
		//Pane for the buttons and volume sliders
		HBox bottomPane = new HBox();
		
		//To layout the buttons in the middle and volume slider to the right
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		Region region2 = new Region();
		region2.setMinWidth(120);
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		bottomPane.getChildren().addAll(region2,_skipBackwardButton, _playPauseButton, _skipForwardButton, region, _volumeSlider);
		
		this.getChildren().addAll(_mediaPlayerPane, _timeSlider, bottomPane);
	}
	
	public void playVideo(Creation creation) {
		File creationToPlay = new File("creations/"+creation.getName()+".mp4");
		
		Media video = new Media(creationToPlay.toURI().toString());
		
		//Stop the previous video playing
		this.stop();
		
		_mediaPlayer = new MediaPlayer(video);
		_mediaPlayer.setAutoPlay(true);
		
		_mediaView = new MediaView(_mediaPlayer);
		
		//Size video to pane
		_mediaView.setPreserveRatio(false);
		_mediaView.fitWidthProperty().bind(_mediaPlayerPane.maxWidthProperty());
		_mediaView.fitHeightProperty().bind(_mediaPlayerPane.maxHeightProperty());
		
		//Replace black box with media view
		_mediaPlayerPane.getChildren().remove(0);
		_mediaPlayerPane.getChildren().addAll(_mediaView);
		
		changeGUIVideoHasBeenPlayed();
	}
	
	/**
	 * Video has been played so replace rectangle with MediaView, enable and add functionality for
	 *  all the buttons and sliders
	 */
	private void changeGUIVideoHasBeenPlayed() {
		//Re-enable buttons and sliders as video is now played
		_playPauseButton.videoPlayed(_mediaPlayer);
		_skipForwardButton.setDisable(false);
		_skipBackwardButton.setDisable(false);
		_volumeSlider.setDisable(false);
		
		_timeSlider.videoPlayed(_mediaPlayer);
		

		

		
		//Functionality for volume slider
		_volumeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				_mediaPlayer.setVolume(_volumeSlider.getValue()/100);
			}
		});
		
//		//Add functionality of Play/pause button
//		_playPauseButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				if(_mediaPlayer.getStatus() == Status.PLAYING) {
//					_mediaPlayer.pause();
//					_playPauseButton.setText("Play");
//				}
//				else {
//					_mediaPlayer.play();
//					_playPauseButton.setText("Pause");
//				}
//			}
//		});
		
		//Functionality of skip forward button
		_skipForwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_mediaPlayer.seek(_mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
			}
		});
		
		//Functionality of skip backward button
		_skipBackwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_mediaPlayer.seek(_mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
			}
		});
	}

	public void stop() {
		if(_mediaPlayer != null) {
			_mediaPlayer.stop();
		}
	}
	
}