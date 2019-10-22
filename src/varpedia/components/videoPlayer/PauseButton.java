package varpedia.components.videoPlayer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.shape.Circle;
import varpedia.home.Main;

/**
 * The play/pause button for the video player
 *
 */
public class PauseButton extends Button{

	private int _width;
	private int _height;
	
	public PauseButton(int width, int height) {
		super();
		_width = width;
		_height = height;
		
		
		playImage();
		
		this.setMaxHeight(_height);
		this.setMaxWidth(_width);
		this.setDisable(true);
	}
	
	/**
	 * Enable, change text and add functionality as video has been played
	 * @param mediaPlayer  the media player binded to this button
	 */
	public void videoPlayed(MediaPlayer mediaPlayer) {
		this.setDisable(false);

		
		//Add functionality for play/pause
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(mediaPlayer.getStatus() == Status.PLAYING) {
					mediaPlayer.pause();
					playImage();
				}
				else {
					mediaPlayer.play();
					pauseImage();
				}
			}
		});
		
		pauseImage();
	}

	private void playImage() {
		try {
			BufferedImage image = ImageIO.read(new File("resources/icons/play.png"));
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
			imageView.fitHeightProperty().set(_height);
			imageView.fitWidthProperty().set(_width);
			this.setGraphic(imageView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void pauseImage() {
		try {
			BufferedImage image = ImageIO.read(new File("resources/icons/pause.png"));
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
			imageView.fitHeightProperty().set(_height);
			imageView.fitWidthProperty().set(_width);
			this.setGraphic(imageView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
