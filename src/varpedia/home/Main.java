package varpedia.home;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import varpedia.helper.Scripts;

public class Main extends Application {
	
	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		cleanUpFiles();
		setUpFiles();
		
		primaryStage.setTitle("VARpedia Home");
		
		Scene scene = new Scene(new Home(primaryStage), 1000, 550);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	
		primaryStage.setOnCloseRequest(e -> {
			cleanUpFiles();
		});
	}
	
	/**
	 * Set up the necessary files
	 */
	private void setUpFiles() {
		//Create creations folder if it's the first time they are using this app
		File creationsFolder = new File("creations");
		creationsFolder.mkdir();
	}
	
	/**
	 * Clean up files when the app is closed
	 */
	private void cleanUpFiles() {
		Scripts scripts = new Scripts();
		scripts.getScript("cleanup", null);
	}
}
