package varpedia;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("VARpedia");
    
		BorderPane layout = new BorderPane();
    
		CreationsViewerScreen a = new CreationsViewerScreen();
		
		Scene scene = new Scene(layout, 1000, 900);
		
		layout.getChildren().addAll(a);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
