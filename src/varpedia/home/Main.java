package varpedia.home;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import varpedia.helper.Scripts;

public class Main extends Application {
	
	/**
	 * Folder for the creations to be put in
	 */
	private File creationsFolder;
	
	/**
	 * Temporary folders
	 */
	private File audioFolder;
	private File imagesFolder;
	private File selectedImagesFolder;
	private File resampledAudioFolder;

	
	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
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
		creationsFolder = new File("creations");
		creationsFolder.mkdir();
		
		//Create temporary folders
		audioFolder = new File("audio");
		audioFolder.mkdir();
		imagesFolder = new File("images");
		imagesFolder.mkdir();
		selectedImagesFolder = new File("selectedImages");
		selectedImagesFolder.mkdir();
		resampledAudioFolder = new File("resampledAudio");
		resampledAudioFolder.mkdir();
	}
	
	/**
	 * Clean up files when the app is closed
	 */
	private void cleanUpFiles() {
		audioFolder.delete();
		imagesFolder.delete();
		selectedImagesFolder.delete();
		resampledAudioFolder.delete();
		
		Scripts scripts = new Scripts();
        scripts.getScript("cleanup", new String[]{});
	}
}
