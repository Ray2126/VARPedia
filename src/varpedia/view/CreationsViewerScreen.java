package varpedia.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class CreationsViewerScreen{
	
	private BorderPane creationsScreen;
	private VideoPlayer videoPlayer;
	private CreationTable creationTable;
	private HBox buttonsPane;
	private Home home;
	private VBox videoWrapper;
	private Label title;
	private VBox tableBox;
	
	public CreationsViewerScreen(Home home) {
		this.home = home;
		creationsScreen = new BorderPane();
		buttonsPane = new HBox(10);
		videoPlayer = new VideoPlayer();
		creationTable = new CreationTable(videoPlayer);
		tableBox = new VBox();
		
		HBox content = new HBox();
		
		Styling.yellowBG(creationsScreen);
		
		initVideoPlayer();
		initHomeButton();
		initTitle();
		initTable();
		
		content.getChildren().addAll(tableBox, videoWrapper);
		
		creationsScreen.setCenter(content);
		creationsScreen.setBottom(buttonsPane);
	}
	
	private void initTable() {
		tableBox.getChildren().addAll(title, creationTable.getTable());
		tableBox.setSpacing(20);
		tableBox.setAlignment(Pos.TOP_CENTER);
		tableBox.setPadding(new Insets(20,0,20,20));
	}
	
	private void initTitle() {
		title = new Label("Your Creations");
		Styling.textColor(title);
		title.setFont(Font.font("Verdana", 30));
	}
	
	private void initVideoPlayer() {
		videoWrapper = new VBox();
		HBox videoBox = new HBox();
		
		videoBox.setAlignment(Pos.CENTER);
		videoBox.setPadding(new Insets(10,10,10,10));
		videoBox.getChildren().addAll(videoPlayer);
		
		videoWrapper.setAlignment(Pos.CENTER);
		videoWrapper.getChildren().add(videoBox);
	}
	
	public BorderPane getScreen() {
		return creationsScreen;
	}
	
	private void initHomeButton() {
		Button homeButton = new Button();
		homeButton.setGraphic(LoadIcon.loadIcon("home", 30, 30));
		Styling.blueButton(homeButton);
		
		homeButton.setOnAction(e -> {
				home.showHome();
		});
		
		//Pane for the buttons
		buttonsPane.setPadding(new Insets(0,10,10,10));
		buttonsPane.setAlignment(Pos.CENTER_RIGHT);
		
		buttonsPane.getChildren().addAll(homeButton);
	}

	
}
