package varpedia.quiz;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import varpedia.components.videoPlayer.VideoPlayer;
import varpedia.helper.Creation;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

/**
 * This is the main screen for the active learning component
 * It presents the video and direct to results when complete
 *
 */
public class QuizQuestionScreen {
	QuizNavigator navigator;
	private VBox quizPanel;
	private VideoPlayer videoPlayer;
	private TextField guessInput;
	private List<String> guesses;
	private List<Creation> creations;
	private int currentVidIndex;
	private HBox navBar;
	private Button backBtn;
	private Button nextBtn;
	private Button cancelBtn;
	HBox options;
	BooleanBinding guessValid;
	
	/**
	 * Initialize class variables, panel formatting and component formatting
	 * @param navigator the navigator that is controlling the process of this component
	 */
	public QuizQuestionScreen(QuizNavigator navigator) {
		this.navigator = navigator;
		quizPanel = new VBox();
		int width = 780;
		HBox videoBox = new HBox();
		guesses = new ArrayList<String>();
		currentVidIndex = 0;
		videoPlayer = new VideoPlayer();
        navBar = new HBox();
        backBtn = new Button("Back");
        cancelBtn = new Button("Cancel");
        nextBtn = new Button("Next");
		
		quizPanel.setMaxWidth(width);
		quizPanel.setMinWidth(width);
		quizPanel.setPrefWidth(width);
		creations = getCreations();
		
        videoBox.setPadding(new Insets(40,40,40,40));
        videoBox.setMaxSize(500,1000);
        videoBox.setAlignment(Pos.CENTER);    
        videoBox.getChildren().addAll(videoPlayer);
        
        initUserInput();
        
        initNavBar();
        
        quizPanel.getChildren().addAll(videoBox, options, navBar);
	}
	
	/**
	 * Initialize the nav bar and button styling and button functionality
	 */
	private void initNavBar() {
		Styling.blueButton(backBtn);
		Styling.blueButton(nextBtn);
        Styling.blueButton(cancelBtn);
        
        backBtn.setFont(Font.font ("Verdana", 15));
        nextBtn.setFont(Font.font ("Verdana", 15));
        cancelBtn.setFont(Font.font ("Verdana", 15));
		
        nextBtn.disableProperty().bind(guessValid.not());
        backBtn.setDisable(true);
        nextBtn.setDefaultButton(true);
        
        backBtn.setOnAction(e -> {
        	if(currentVidIndex != 0) {
        		currentVidIndex--;
        		playNextVid();
        	}
        	if(currentVidIndex == 0) {
        		backBtn.setDisable(true);
        	}
        });
        
        cancelBtn.setOnAction(e -> {
        	videoPlayer.stop();
        	navigator.closeQuiz();
        });
        
        nextBtn.setOnAction(e -> next());
        
        navBar.setPadding(new Insets(10,10,10,10));
        navBar.setSpacing(10);
        navBar.setAlignment(Pos.CENTER_RIGHT);
        
        navBar.getChildren().addAll(backBtn, nextBtn, cancelBtn);
	}
	
	/**
	 * Functionality of the next button, either next quiz button or the feedback screen
	 */
	private void next() {
    	backBtn.setDisable(false);
    	guesses.add(currentVidIndex,guessInput.getText().toLowerCase());
    	if(currentVidIndex < creations.size()-1) {
        	currentVidIndex++;
        	playNextVid();	
    	}else {
    		//Last video is played so show feedback screen
    		videoPlayer.stop();
    		backBtn.setDisable(true);
    		quizPanel.getChildren().clear();
    		FeedbackScreen fb = new FeedbackScreen(creations, guesses, navigator);
    		navigator.showFeedback(fb);
    	}
	}

	/**
	 * Initialize the user input prompt and and area also creating 
	 * the binding of whether input is added
	 */
	private void initUserInput() {
		options = new HBox();
		Label what = new Label("What is this videos topic? ");
		Styling.textColor(what);
		what.setFont(Font.font("Verdana", 20));
        options.setAlignment(Pos.CENTER);
        guessInput = new TextField();
        Styling.textField(guessInput);
        Styling.yellowBG(quizPanel);
        guessInput.requestFocus();
        guessValid = Bindings.createBooleanBinding(() -> {
            if(guessInput.getText().isEmpty()) {
            	return false;
            }
            return true;
        }, guessInput.textProperty());
        options.getChildren().addAll(what, guessInput);
        options.setSpacing(10);
        options.setPadding(new Insets(10,10,10,10));
	}
	
	/**
	 * Begin showing the quiz videos and get quiz component
	 * @return returns the quiz with video playing
	 */
	public VBox startQuiz() {
		currentVidIndex = 0;
		videoPlayer.playQuizVideo(creations.get(0).getName());
		return quizPanel;
	}
	
	/**
	 * Plays the next creation
	 */
	private void playNextVid() {
		guessInput.clear();
		videoPlayer.stop();
		videoPlayer.playQuizVideo(creations.get(currentVidIndex).getName());
	}
	
	/**
	 * Retrieves all creations from files and converts to java objects
	 * @return List of all the users save creations
	 */
	private List<Creation> getCreations(){
		List<Creation> creations = new ArrayList<Creation>();
        Scripts scripts =new Scripts();
        scripts.getScript("listCreations", new String[]{});
        
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream(".temp/listing");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			BufferedReader Buff;
			String strLine;
			//Read File Line By Line
			int index=0;
			while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
				Buff = new BufferedReader(new FileReader("./creations/"+strLine+"/quiz/searchTerm.text"));
				String text = Buff.readLine();
				strLine = strLine.replace("./creations/", "");
				strLine = strLine.replace(".mp4", "");

				creations.add(new Creation(strLine, ++index, text));
			}
	
			//Close the input stream
			fstream.close();
		}catch(Exception e) {	
		}
		return creations;
	}

	/**
	 * Whether the user has any creations or not
	 * @return true: creations exist false: no creations
	 */
	public boolean hasCreations() {
		if(creations.size() == 0) {
			return false;
		}
		return true;
	}
}
