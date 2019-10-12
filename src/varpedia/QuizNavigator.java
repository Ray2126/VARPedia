package varpedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
	
	private HBox navBar;
	private Button backBtn;
	private Button nextBtn;
	private Button cancelBtn;
	
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
        actual = new ArrayList<String>();
        getActual();
        
        
        
        currentVidIndex = 0;
        HBox options = new HBox();
        Label what = new Label("What is this videos topic? ");
        what.setFont(Font.font ("Verdana", 20));
        options.setAlignment(Pos.CENTER);
        guessInput = new TextField();
        guessInput.setStyle("-fx-font: 16px \"Verdana\";");
        BooleanBinding guessValid = Bindings.createBooleanBinding(() -> {
            if(guessInput.getText().isEmpty()) {
            	return false;
            }
            return true;
        }, guessInput.textProperty());
        options.getChildren().addAll(what, guessInput);
        options.setSpacing(10);
        options.setPadding(new Insets(10,10,10,10));
        
        
        //Initialize nav bar
        navBar = new HBox();
        backBtn = new Button("Back");
        backBtn.setDisable(true);
        backBtn.setOnAction(e -> {
        	if(currentVidIndex != 0) {
        		currentVidIndex--;
        		playNextVid();
        	}
        	if(currentVidIndex == 0) {
        		backBtn.setDisable(true);
        	}
        });
        backBtn.setFont(Font.font ("Verdana", 15));
        nextBtn = new Button("Next");
        nextBtn.setDefaultButton(true);
        nextBtn.disableProperty().bind(guessValid.not());
        nextBtn.setFont(Font.font ("Verdana", 15));
        cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font ("Verdana", 15));
        cancelBtn.setOnAction(e -> {
        	videoPlayer.stop();
        	home.showHome();
        });
        
        nextBtn.setOnAction(e -> {
        	backBtn.setDisable(false);
        	guesses.add(currentVidIndex,guessInput.getText());
        	if(currentVidIndex < creations.size()-1) {
            	currentVidIndex++;
            	playNextVid();	
        	}else {
        		//Last video is played so show feedback screen
        		videoPlayer.stop();
        		backBtn.setDisable(true);
        		quizPanel.getChildren().clear();
        		FeedbackScreen fb = new FeedbackScreen(creations, guesses, actual, home);
        		quizPanel.setAlignment(Pos.CENTER);
        		quizPanel.getChildren().addAll(fb.getScreen());
        	}
        });
        
        //Navbar style
        navBar.setPadding(new Insets(10,10,10,10));
        navBar.setSpacing(10);
        navBar.setAlignment(Pos.CENTER_RIGHT);
        
        navBar.getChildren().addAll(backBtn, nextBtn, cancelBtn);
        
        quizPanel = new VBox();
        quizPanel.getChildren().addAll(videoBox, options, navBar);
	}
	
	private void getActual() {
		for(String creation : creations) {
	        BufferedReader Buff;
			try {
				Buff = new BufferedReader(new FileReader("./creations/"+creation+"/quiz/searchTerm.text"));
		        String text = Buff.readLine();
		        actual.add(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public VBox getQuiz() {
		currentVidIndex = 0;
		videoPlayer.playQuizVideo(creations.get(0));
		return quizPanel;
	}
	
	private void playNextVid() {
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
