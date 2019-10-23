package varpedia.components.videoPlayer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import varpedia.components.tables.Creation;

/**
 * Video player seen on the main screen and preview screen
 */
public class VideoPlayer extends VBox{

	private PauseButton _playPauseButton;
	private Button _skipForwardButton;
	private Button _skipBackwardButton;
	private TimeSlider _timeSlider;
	private VBox _mediaPlayerPane;
	private MediaPlayer _mediaPlayer;
	private MediaView _mediaView;
	private Label _durationVideo;
	private VolumeControl _volume;
	
	public VideoPlayer() {
		_playPauseButton = new PauseButton(30,30);
		_skipForwardButton = new Button();
		try {
			BufferedImage image = ImageIO.read(new File("resources/icons/skipForward.png"));
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
			imageView.fitHeightProperty().set(30);
			imageView.fitWidthProperty().set(30);
			_skipForwardButton.setGraphic(imageView);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_skipBackwardButton = new Button();
		try {
			BufferedImage image = ImageIO.read(new File("resources/icons/skipBackward.png"));
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
			imageView.fitHeightProperty().set(30);
			imageView.fitWidthProperty().set(30);
			_skipBackwardButton.setGraphic(imageView);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_timeSlider = new TimeSlider();
		
		_durationVideo = new Label("00:00");
		_durationVideo.setStyle("-fx-font: 16px \"Verdana\";");
		
		//Disable buttons when video is not played yet
		_skipForwardButton.setDisable(true);
		_skipBackwardButton.setDisable(true);
		
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
		
		_volume = new VolumeControl();
		bottomPane.getChildren().addAll(region2,_skipBackwardButton, _playPauseButton, _skipForwardButton, region, _volume);
		
		HBox sliderPane = new HBox();
		_timeSlider.setMinWidth(950);
		_timeSlider.setMaxWidth(950);
		_timeSlider.setPrefWidth(950);
		sliderPane.getChildren().addAll(_timeSlider, _durationVideo);
		
		this.getChildren().addAll(_mediaPlayerPane, sliderPane, bottomPane);
	}
	
	/**
	 * Play a creation
	 * @param creation   the creation to be played
	 */
	public void playVideo(Creation creation) {
		File creationToPlay = new File("creations/"+creation.getName()+"/"+creation.getName()+".mp4");

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
	 * Play a creation
	 * @param creation   the creation to be played
	 */
	public void playPreview() {
		File creationToPlay = new File("temp/preview/preview.mp4");
		
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
	 * Play a creation
	 * @param creation   the creation to be played
	 */
	public void playQuizVideo(String name) {
		File creationToPlay = new File("creations/"+name+"/quiz/noText.mp4");
		
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
		_skipForwardButton.setDisable(false);
		_skipBackwardButton.setDisable(false);
		_timeSlider.videoPlayed(_mediaPlayer);
		_playPauseButton.videoPlayed(_mediaPlayer);
		_volume.videoPlayed(_mediaPlayer);
		
		//Duration of video
		_mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				String time = "";
				time += String.format("%02d", (int)newValue.toMinutes());
				time += ":";
				time += String.format("%02d", (int)newValue.toSeconds());
				_durationVideo.setText(time);
			}
		});
		
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

	/**
	 * Stop the media player
	 */
	public void stop() {
		if(_mediaPlayer != null) {
			_mediaPlayer.stop();
		}
	}
	
	public MediaPlayer getMediaPlayer() {
		return _mediaPlayer;
	}
	
	/**
	 * Reverts video player back to default state(not playing video)
	 */
	public void disposeMedia() {
		_mediaPlayerPane.getChildren().remove(_mediaView);
		
		//Set a black box as a placeholder for the video
		Rectangle r = new Rectangle(1000,500);
		_mediaPlayerPane.getChildren().addAll(r);
		
		_playPauseButton.setDisable(true);
		_skipForwardButton.setDisable(true);
		_skipBackwardButton.setDisable(true);
		_timeSlider.setDisable(true);
	}
	
}
