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
 * Video player seen on the main screen, quiz screen and preview screen
 */
public class VideoPlayer extends VBox{
	
	private VBox mediaPlayerPane;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private PauseButton playPauseButton;
	private Button skipForwardButton;
	private Button skipBackwardButton;
	private TimeSlider timeSlider;
	private Label durationVideo;
	private VolumeControl volume;
	private Creation currentPlayingCreation;
	private int width = 700;
	
	public VideoPlayer() {
		this.setPadding(new Insets(20,20,0,20));
		
		setUpMediaPane();
		setUpSliderPane();
		setUpButtonPane();
	}
	
	/**
	 * Set up the place where the video will be. Set a placeholder of a blue box for now.
	 */
	private void setUpMediaPane() {
		mediaPlayerPane = new VBox();
		
		//Set a box as a placeholder for the video
		Rectangle r = new Rectangle(width,500);
		r.setStyle("-fx-fill: #36b5f5");
		mediaPlayerPane.getChildren().addAll(r);
		
		mediaPlayerPane.setMinHeight(500);
		mediaPlayerPane.setMaxHeight(500);
		mediaPlayerPane.setPrefHeight(500);
		mediaPlayerPane.setMinWidth(width);
		mediaPlayerPane.setMaxWidth(width);
		mediaPlayerPane.setPrefWidth(width);
		this.getChildren().add(mediaPlayerPane);
	}
	
	/**
	 * Set up the time slider and the duration label
	 */
	private void setUpSliderPane() {
		timeSlider = new TimeSlider();
		timeSlider.setMinWidth(width-50);
		timeSlider.setMaxWidth(width-50);
		timeSlider.setPrefWidth(width-50);
		
		durationVideo = new Label("00:00");
		durationVideo.setStyle("-fx-font: 16px \"Verdana\";");
		
		HBox sliderPane = new HBox();
		sliderPane.getChildren().addAll(timeSlider, durationVideo);
		this.getChildren().add(sliderPane);
	}
	
	/**
	 * Set up the skip buttons, play button and volume control
	 */
	private void setUpButtonPane() {
		playPauseButton = new PauseButton(30,30);
		
		skipForwardButton = new Button();
		Styling.blueButton(skipForwardButton);;
		skipForwardButton.setGraphic(LoadIcon.loadIcon("skipForward", 30, 30));
		
		skipBackwardButton = new Button();
		Styling.blueButton(skipBackwardButton);
		skipBackwardButton.setGraphic(LoadIcon.loadIcon("skipBackward", 30, 30));
		
		//Disable buttons when video is not played yet
		skipForwardButton.setDisable(true);
		skipBackwardButton.setDisable(true);
		
		HBox bottomPane = new HBox();
		
		//To layout the buttons in the middle and volume to the right
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
		this.getChildren().add(bottomPane);
	}
	
	/**
	 * Play a creation on the view creations screen
	 * @param creation	the creation to be played
	 */
	public void playVideo(Creation creation) {
		File creationToPlay = new File("creations/"+creation.getName()+"/"+creation.getName()+".mp4");
		currentPlayingCreation = creation;
		Media video = new Media(creationToPlay.toURI().toString());
		setUpMediaPlayerView(video);
	}
	
	/**
	 * Play a preview of the creation for the user on the preview screen
	 */
	public void playPreview() {
		File creationToPlay = new File(".temp/preview/preview.mp4");
		Media video = new Media(creationToPlay.toURI().toString());
		setUpMediaPlayerView(video);
	}
	
	/**
	 * Play the quiz video on the learning game screen
	 * @param name	name of creation
	 */
	public void playQuizVideo(String name) {
		File creationToPlay = new File("creations/"+name+"/quiz/noText.mp4");
		Media video = new Media(creationToPlay.toURI().toString());
		setUpMediaPlayerView(video);
	}
	
	/**
	 * Video has been requested to be played so set up media player and media view
	 * @param video		media to be played
	 */
	private void setUpMediaPlayerView(Media video) {
		//Stop the previous video playing
		this.stop();
		
		mediaPlayer = new MediaPlayer(video);
		mediaPlayer.setAutoPlay(true);
		
		mediaView = new MediaView(mediaPlayer);
		
		//Size video to pane
		mediaView.setPreserveRatio(false);
		mediaView.fitWidthProperty().bind(mediaPlayerPane.maxWidthProperty());
		mediaView.fitHeightProperty().bind(mediaPlayerPane.maxHeightProperty());
		
		//Replace box with media view
		mediaPlayerPane.getChildren().remove(0);
		mediaPlayerPane.getChildren().addAll(mediaView);
		
		changeGUIVideoHasBeenPlayed();
	}
	
	/**
	 * Video has been played so replace rectangle with MediaView, enable and add functionality for
	 *  all the buttons and sliders
	 */
	private void changeGUIVideoHasBeenPlayed() {
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
		
		//Make video "reset" when it gets to end
		mediaPlayer.setOnEndOfMedia(new Runnable(){
			@Override
			public void run() {
				mediaPlayer.seek(new Duration(0));
				mediaPlayer.pause();
				timeSlider.setValue(0);
			}
		});
	}

	public void stop() {
		if(mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	public Creation getCurrentPlayingCreation() {
		return currentPlayingCreation;
	}
	
	/**
	 * Reverts video player back to default state(not playing video)
	 */
	public void disposeMedia() {
		mediaPlayerPane.getChildren().remove(mediaView);
		mediaPlayer.stop();
		
		//Set a black box as a placeholder for the video
		Rectangle r = new Rectangle(width,500);
		r.setStyle("-fx-fill: #36b5f5");
		mediaPlayerPane.getChildren().addAll(r);
		
		playPauseButton.setDisable(true);
		skipForwardButton.setDisable(true);
		skipBackwardButton.setDisable(true);
		timeSlider.setDisable(true);
	}
	
}
