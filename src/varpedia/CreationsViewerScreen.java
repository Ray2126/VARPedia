package varpedia;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import varpedia.videoPlayer.VideoPlayer;

/**
 * The view creations screen containing the video player, creation table and buttons to play, 
 * delete and return home.
 *
 */
public class CreationsViewerScreen extends VBox{
	
	private VideoPlayer _videoPlayer;
	private CreationTable _creationTable;
	private Button homeButton;
	private Home home;
	
	public CreationsViewerScreen(Home home) {
		super(10);
		
		this.home = home;
		
		_videoPlayer = new VideoPlayer();
		
		//Pane for the video to sit in
		HBox videoBox = new HBox();
		videoBox.setPadding(new Insets(10,10,10,10));
		videoBox.setAlignment(Pos.CENTER);
		videoBox.getChildren().addAll(_videoPlayer);
		
		
		_creationTable = new CreationTable(_videoPlayer);
		_creationTable.setMaxWidth(980);
		
		//Pane for table of creations
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(_creationTable);
		tableBox.setAlignment(Pos.CENTER);
		tableBox.setPadding(new Insets(10,10,10,10));
		
		
		homeButton = new Button();
		try {
			BufferedImage image = ImageIO.read(new File("resources/icons/home.png"));
			ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
			imageView.fitHeightProperty().set(30);
			imageView.fitWidthProperty().set(30);
			homeButton.setGraphic(imageView);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		homeButton.setOnAction(e -> {
				home.showHome();
		});
		
		//Pane for the buttons
		HBox buttonsPane = new HBox(10);
		buttonsPane.setPadding(new Insets(10,10,10,10));
		buttonsPane.setMaxWidth(980);
		this.setAlignment(Pos.CENTER);
		
		//Anchor home button to right side of HBox
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		buttonsPane.getChildren().addAll(region, homeButton, region2);
		
		this.setHeight(900);
		this.setWidth(1200);
		
		this.getChildren().addAll(videoBox,tableBox, buttonsPane);
	}

	
}
