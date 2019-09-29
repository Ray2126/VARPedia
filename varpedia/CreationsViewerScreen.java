package varpedia;

import java.io.File;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The main screen containing the video player, creation table and buttons to play, delete and create creations.
 *
 */
public class CreationsViewerScreen extends VBox{
	
	private VideoPlayer _videoPlayer;
	private CreationTable _creationTable;
	private Button _playButton;
	private Button _deleteButton;
	private Button _createButton;
	
	public CreationsViewerScreen() {
		super(10);
		
		_videoPlayer = new VideoPlayer();
		
		//Pane for the video to sit in
		HBox videoBox = new HBox();
		videoBox.setPadding(new Insets(10,10,10,10));
		videoBox.setAlignment(Pos.CENTER);
		videoBox.getChildren().addAll(_videoPlayer);
		
		
		_creationTable = new CreationTable();
		_creationTable.setMaxWidth(980);
		
		//Pane for table of creations
		VBox tableBox = new VBox();
		tableBox.getChildren().addAll(_creationTable);
		tableBox.setAlignment(Pos.CENTER);
		tableBox.setPadding(new Insets(10,10,10,10));
		
		
		_playButton = new Button("Play");
		_deleteButton = new Button("Delete");
		_createButton = new Button("Create");
		
		_playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_videoPlayer.playVideo(_creationTable.getSelectionModel().getSelectedItem());
			}
		});
		
		_deleteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Creation selectedItem = _creationTable.getSelectionModel().getSelectedItem();
				
				if(selectedItem != null) {
					//Create a dialog for user to confirm their delete selection
					Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
					((Button) confirmAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
					confirmAlert.setTitle("Confirmation");
					confirmAlert.setHeaderText(null);
					confirmAlert.setContentText("You are about to delete " + selectedItem.getName() + ". Are you sure?");
					Optional<ButtonType> result = confirmAlert.showAndWait();
					if(result.get() == ButtonType.OK) {
						File file = new File("creations/" + selectedItem.getName() + ".mp4");
						file.delete();
					}
				}
				
				_creationTable.loadCreations();
				_videoPlayer.stop();

			}
			
		});
		
		_createButton.setOnAction(e -> {
				CreatorMain c = new CreatorMain(_creationTable);
				c.beginCreate();
		});
		
		//Pane for the buttons
		HBox buttonsPane = new HBox(10);
		buttonsPane.setPadding(new Insets(10,10,10,10));
		buttonsPane.setMaxWidth(980);
		this.setAlignment(Pos.CENTER);
		
		//Anchor create button to right side of HBox
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		buttonsPane.getChildren().addAll(_playButton, _deleteButton, region, _createButton);
		
		this.setHeight(900);
		this.setWidth(1200);
		
		this.getChildren().addAll(videoBox,tableBox, buttonsPane);
	}
}
