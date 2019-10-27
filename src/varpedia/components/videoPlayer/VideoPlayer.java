package varpedia.components.videoPlayer;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import varpedia.helper.Creation;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

/**
 * Video player seen on the main screen and preview screen
 */
public class VideoPlayer extends VBox{

	private PauseButton playPauseButton;
	private Button skipForwardButton;
	private Button skipBackwardButton;
	private TimeSlider timeSlider;
	private VBox mediaPlayerPane;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private Label durationVideo;
	private VolumeControl volume;
	private int width = 700;
	
	public VideoPlayer() {
		playPauseButton = new PauseButton(30,30);
		skipForwardButton = new Button();
		Styling.blueButton(skipForwardButton);;
		skipForwardButton.setGraphic(LoadIcon.loadIcon("skipForward", 30, 30));
		skipBackwardButton = new Button();
		Styling.blueButton(skipBackwardButton);
		skipBackwardButton.setGraphic(LoadIcon.loadIcon("skipBackward", 30, 30));
		timeSlider = new TimeSlider();
		
		durationVideo = new Label("00:00");
		durationVideo.setStyle("-fx-font: 16px \"Verdana\";");
		
		//Disable buttons when video is not played yet
		skipForwardButton.setDisable(true);
		skipBackwardButton.setDisable(true);
		
		mediaPlayerPane = new VBox();
		
		//Set a black box as a placeholder for the video
		Rectangle r = new Rectangle(width,500);
		r.setStyle("-fx-fill: #36b5f5");
		mediaPlayerPane.getChildren().addAll(r);
		
		//Set size of video player pane
		mediaPlayerPane.setMinHeight(500);
		mediaPlayerPane.setMaxHeight(500);
		mediaPlayerPane.setPrefHeight(500);
		mediaPlayerPane.setMinWidth(width);
		mediaPlayerPane.setMaxWidth(width);
		mediaPlayerPane.setPrefWidth(width);
		
		this.setPadding(new Insets(20,20,0,20));
		
		//Pane for the buttons and volume sliders
		HBox bottomPane = new HBox();
		
		//To layout the buttons in the middle and volume slider to the right
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		Region region2 = new Region();
		region2.setMinWidth(120);
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		volume = new VolumeControl();
		bottomPane.setPadding(new Insets(10,10,10,10));
		bottomPane.setSpacing(10);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.getChildren().addAll(region2,skipBackwardButton, playPauseButton, skipForwardButton, region, volume);
		
		HBox sliderPane = new HBox();
		timeSlider.setMinWidth(width-50);
		timeSlider.setMaxWidth(width-50);
		timeSlider.setPrefWidth(width-50);
		sliderPane.getChildren().addAll(timeSlider, durationVideo);
		
		this.getChildren().addAll(mediaPlayerPane, sliderPane, bottomPane);
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
		
		mediaPlayer = new MediaPlayer(video);
		mediaPlayer.setAutoPlay(true);
		
		mediaView = new MediaView(mediaPlayer);
		
		//Size video to pane
		mediaView.setPreserveRatio(false);
		mediaView.fitWidthProperty().bind(mediaPlayerPane.maxWidthProperty());
		mediaView.fitHeightProperty().bind(mediaPlayerPane.maxHeightProperty());
		
		//Replace black box with media view
		mediaPlayerPane.getChildren().remove(0);
		mediaPlayerPane.getChildren().addAll(mediaView);
		
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
		
		mediaPlayer = new MediaPlayer(video);
		mediaPlayer.setAutoPlay(true);
		
		mediaView = new MediaView(mediaPlayer);
		
		//Size video to pane
		mediaView.setPreserveRatio(false);
		mediaView.fitWidthProperty().bind(mediaPlayerPane.maxWidthProperty());
		mediaView.fitHeightProperty().bind(mediaPlayerPane.maxHeightProperty());
		
		//Replace black box with media view
		mediaPlayerPane.getChildren().remove(0);
		mediaPlayerPane.getChildren().addAll(mediaView);
		
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
		
		mediaPlayer = new MediaPlayer(video);
		mediaPlayer.setAutoPlay(true);
		
		mediaView = new MediaView(mediaPlayer);
		
		//Size video to pane
		mediaView.setPreserveRatio(false);
		mediaView.fitWidthProperty().bind(mediaPlayerPane.maxWidthProperty());
		mediaView.fitHeightProperty().bind(mediaPlayerPane.maxHeightProperty());
		
		//Replace black box with media view
		mediaPlayerPane.getChildren().remove(0);
		mediaPlayerPane.getChildren().addAll(mediaView);
		
		changeGUIVideoHasBeenPlayed();
	}
	
	/**
	 * Video has been played so replace rectangle with MediaView, enable and add functionality for
	 *  all the buttons and sliders
	 */
	private void changeGUIVideoHasBeenPlayed() {
		//Re-enable buttons and sliders as video is now played
		skipForwardButton.setDisable(false);
		skipBackwardButton.setDisable(false);
		timeSlider.videoPlayed(mediaPlayer);
		playPauseButton.videoPlayed(mediaPlayer);
		volume.videoPlayed(mediaPlayer);
		
		//Duration of video
		mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				String time = "";
				time += String.format("%02d", (int)newValue.toMinutes());
				time += ":";
				time += String.format("%02d", (int)newValue.toSeconds());
				durationVideo.setText(time);
			}
		});
		
		//Functionality of skip forward button
		skipForwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
			}
		});
		
		//Functionality of skip backward button
		skipBackwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
			}
		});
	}

	/**
	 * Stop the media player
	 */
	public void stop() {
		if(mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	/**
	 * Reverts video player back to default state(not playing video)
	 */
	public void disposeMedia() {
		mediaPlayerPane.getChildren().remove(mediaView);
		
		//Set a black box as a placeholder for the video
		Rectangle r = new Rectangle(width,500);
		mediaPlayerPane.getChildren().addAll(r);
		
		playPauseButton.setDisable(true);
		skipForwardButton.setDisable(true);
		skipBackwardButton.setDisable(true);
		timeSlider.setDisable(true);
	}
	
}
