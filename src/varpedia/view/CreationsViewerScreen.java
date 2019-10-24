package varpedia.view;

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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import varpedia.components.videoPlayer.VideoPlayer;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;
import varpedia.home.Home;

/**
 * The view creations screen containing the video player, creation table and buttons to play, 
 * delete and return home.
 *
 */
public class CreationsViewerScreen extends HBox{
	
	private VideoPlayer _videoPlayer;
	private CreationTable _creationTable;
	private Button homeButton;
	private Home home;
	
	public CreationsViewerScreen(Home home) {
		super(10);
		
		this.home = home;
		Styling.yellowBG(this);
		
		_videoPlayer = new VideoPlayer();
		
		//Pane for the video to sit in
		VBox videoWrapper = new VBox();
		videoWrapper.setAlignment(Pos.CENTER);
		HBox videoBox = new HBox();
		videoWrapper.getChildren().add(videoBox);
		videoBox.setAlignment(Pos.CENTER);
		videoBox.setPadding(new Insets(10,10,10,10));
		videoBox.getChildren().addAll(_videoPlayer);
		
		Label title = new Label("Your Creations");
		Styling.textColor(title);
		title.setFont(Font.font("Verdana", 30));
		_creationTable = new CreationTable(_videoPlayer);
		_creationTable.setMaxWidth(455);
		
		//Pane for table of creations
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(title, _creationTable);
		tableBox.setSpacing(20);
		tableBox.setAlignment(Pos.CENTER);
		tableBox.setPadding(new Insets(10,10,10,10));
		
		
		homeButton = new Button();
		homeButton.setGraphic(LoadIcon.loadIcon("home", 30, 30));
		Styling.blueButton(homeButton);
		
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
		this.setWidth(700);
		
		this.getChildren().addAll(tableBox, videoWrapper,buttonsPane);
	}

	
}
