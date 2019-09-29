package varpedia;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import varpedia.creating.Scripts;

public class Main extends Application{

	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Make file for the creations if it doesnt exist
		File f = new File("creations");
		f.mkdir();
		f = new File("audio");
		f.mkdir();
		f = new File("images");
		f.mkdir();
		f = new File("selectedImages");
		f.mkdir();
		f = new File("resampledAudio");
		f.mkdir();
		Scripts scripts = new Scripts();
        scripts.getScript("cleanup", new String[]{});
		
		primaryStage.setTitle("VARpedia");
    
		BorderPane layout = new BorderPane();
    
		CreationsViewerScreen a = new CreationsViewerScreen();
		
		Scene scene = new Scene(layout, 1100, 1000);
		
		layout.setCenter(a);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
