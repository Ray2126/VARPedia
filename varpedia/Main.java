package varpedia;

import java.io.File;

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
		File f = new File("creations");
		f.mkdir();
		
		primaryStage.setTitle("VARpedia");
    
		BorderPane layout = new BorderPane();
    
		CreationsViewerScreen a = new CreationsViewerScreen();
		
		Scene scene = new Scene(layout, 1100, 1000);
		
		layout.setCenter(a);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
