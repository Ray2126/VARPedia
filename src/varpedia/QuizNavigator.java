package varpedia;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import varpedia.videoPlayer.VideoPlayer;

public class QuizNavigator {
	private VBox quizPanel;
	private int creationsAmount;
	private VideoPlayer videoPlayer;
	private TextField guessInput;
	private List<String> guesses;
	private List<String> creations;
	private List<String> actual;
	private int currentVidIndex;
	private Home home;
	
	public QuizNavigator(Home home) {
		this.home = home;
		creationsAmount = getAmount();
		creations = getCreations();
		
		HBox videoBox = new HBox();
        videoBox.setPadding(new Insets(40,40,40,40));
        videoBox.setMaxSize(500,1000);
        videoBox.setAlignment(Pos.CENTER);
        videoPlayer = new VideoPlayer();
        videoBox.getChildren().addAll(videoPlayer);
        
        guesses = new ArrayList<String>();
        
        currentVidIndex = 0;
        HBox options = new HBox();
        options.setAlignment(Pos.CENTER);
        guessInput = new TextField();
        Button next = new Button("Next");
        options.getChildren().addAll(guessInput, next);
        options.setSpacing(10);
        options.setPadding(new Insets(10,10,10,10));
        next.setOnAction(e -> {
        	if(currentVidIndex < creations.size()-1) {
            	currentVidIndex++;
            	playNextVid();	
        	}else {
        		//Last video is played so show feedback screen
        	}
        });
        
        
        quizPanel = new VBox();
        quizPanel.getChildren().addAll(videoBox, options);
	}
	
	public VBox getQuiz() {
		if(creations.size() == 0) {
			return null;
		}
		currentVidIndex = 0;
		videoPlayer.playQuizVideo(creations.get(0));
		return quizPanel;
	}
	
	private void playNextVid() {
		guesses.add(guessInput.getText());
		guessInput.clear();
		videoPlayer.stop();
		videoPlayer.playQuizVideo(creations.get(currentVidIndex));
	}
	
	private List<String> getCreations(){
		File f = new File("./creations/");
		ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
		return names;
	}
	
	private int getAmount() {
		File file = new File("./creations/");
		File[] files = file.listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File f) {
		        return f.isDirectory();
		    }
		});
		return files.length;
	}
}
