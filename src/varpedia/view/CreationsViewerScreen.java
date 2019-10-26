package varpedia.view;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
public class CreationsViewerScreen extends BorderPane{
	
	private VideoPlayer _videoPlayer;
	private CreationTable _creationTable;
	private Button homeButton;
	
	public CreationsViewerScreen(Home home) {
		super();
		
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
		tableBox.setAlignment(Pos.TOP_CENTER);
		tableBox.setPadding(new Insets(50,10,10,10));
		
		
		homeButton = new Button();
		homeButton.setGraphic(LoadIcon.loadIcon("home", 30, 30));
		Styling.blueButton(homeButton);
		
		homeButton.setOnAction(e -> {
				home.showHome();
		});
		
		//Pane for the buttons
		HBox buttonsPane = new HBox(10);
		buttonsPane.setPadding(new Insets(0,10,10,10));
		buttonsPane.setAlignment(Pos.CENTER_RIGHT);
		
		buttonsPane.getChildren().addAll(homeButton);
		
		HBox view = new HBox();
		view.getChildren().addAll(tableBox, videoWrapper);
		this.setCenter(view);
		this.setBottom(buttonsPane);
	}

	
}
