package varpedia;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import varpedia.creating.Scripts;

public class Main extends Application {
	
	/**
	 * Folder for the creations to be put in
	 */
	private File _creationsFolder;
	
	/**
	 * Temporary folders
	 */
	private File _audioFolder;
	private File _imagesFolder;
	private File _selectedImagesFolder;
	private File _resampledAudioFolder;

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
		_creationsFolder = new File("creations");
		_creationsFolder.mkdir();
		
		//Create temporary folders
		_audioFolder = new File("audio");
		_audioFolder.mkdir();
		_imagesFolder = new File("images");
		_imagesFolder.mkdir();
		_selectedImagesFolder = new File("selectedImages");
		_selectedImagesFolder.mkdir();
		_resampledAudioFolder = new File("resampledAudio");
		_resampledAudioFolder.mkdir();
	}
	
	/**
	 * Clean up files when the app is closed
	 */
	private void cleanUpFiles() {
		_audioFolder.delete();
		_imagesFolder.delete();
		_selectedImagesFolder.delete();
		_resampledAudioFolder.delete();
		
		Scripts scripts = new Scripts();
        scripts.getScript("cleanup", new String[]{});
	}
}
