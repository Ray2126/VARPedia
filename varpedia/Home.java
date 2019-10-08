package varpedia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Home extends VBox{
	private Label title;
	private Button play, creations, newCreation, help, exit;
	Stage primaryStage;
	Stage creationsStage;
	
	public Home(Stage primaryStage) {
		title = new Label("VARpedia");
		title.setFont(new Font("Arial", 30));
		
		play = new Button("Play Learning Game");
		creations = new Button("View My Creations");
		setCreations();
		newCreation = new Button("Create New");
		setNewCreation();
		help = new Button("Get help");
		exit = new Button("Quit");
		setExit();
		
		this.getChildren().addAll(title, play, creations, newCreation, help, exit);
		
		this.primaryStage = primaryStage;
		creationsStage = new Stage();
		
		this.setSpacing(20);
		this.setPadding(new Insets(20,20,20,20));
		this.setAlignment(Pos.CENTER_LEFT);
	}
	
	public void showHome() {
		if(creationsStage.isShowing()) {
			creationsStage.close();
		}
		primaryStage.show();
	}
	
	private void setCreations() {
		creations.setOnAction(e ->{
			creationsStage.sizeToScene();
			BorderPane layout = new BorderPane();
		    
			CreationsViewerScreen creations = new CreationsViewerScreen(this);
			
			layout.setCenter(creations);
			Scene creationsScene = new Scene(layout);
			creationsStage.setScene(creationsScene);
			primaryStage.close();
			creationsStage.show();
		});
	}
	
	private void setNewCreation() {
		newCreation.setOnAction(e ->{
			CreatorMain creator = new CreatorMain(this);
			primaryStage.close();
			creator.beginCreate();
		});
	}
	
	private void setExit() {
		exit.setOnAction(e ->{
			primaryStage.close();
		});
	}
}