package varpedia.quiz;

import java.io.File;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import varpedia.helper.Creation;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;

/**
 * Manages the feedback provided to the user based on actual creations and their guesses
 * Then displaying this info to the user
 *
 */
public class FeedbackScreen {
	
	private GridPane layout;
	private BorderPane panel;
	private HBox navBar;
	private Button homeBtn;
	private QuizNavigator navigator;
	
	private List<Creation> creations;
	private List<String> guesses;
	
	public FeedbackScreen(List<Creation> creations, List<String> guesses, QuizNavigator navigator) {
		
		this.navigator = navigator;
		this.creations = creations;
		this.guesses = guesses;
		layout = new GridPane();
		panel = new BorderPane();
		
        initGrid();
        initNavBar();
		addLabels();
		addRows();
		
		Styling.yellowBG(panel);
        panel.setPrefHeight(793);
        panel.setTop(layout);
        panel.setBottom(navBar);
	}
	
	/**
	 * Initializes the grid layout for the feedback with padding and design
	 */
	private void initGrid() {
		layout.setPadding(new Insets(20,20,20,20));
		Styling.yellowBG(layout);
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(50);
		layout.setVgap(30);
		layout.setMaxWidth(1000);
	}
	
	/**
	 * Initializes the navigation component and applying the functionality 
	 */
	private void initNavBar() {
        navBar = new HBox();
        homeBtn = new Button();
        Styling.blueButton(homeBtn);
        homeBtn.setGraphic(LoadIcon.loadIcon("home", 30, 30));
        homeBtn.setFont(Font.font ("Verdana", 15));
        homeBtn.setOnAction(e -> {
        	navigator.closeQuiz();
        });
        
        navBar.setPadding(new Insets(10,10,10,10));
        navBar.setSpacing(10);
        navBar.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setAlignment(navBar, Pos.CENTER_RIGHT);
        
        navBar.getChildren().addAll(homeBtn);
	}
	
	/**
	 * Recursively add feedback for each video
	 */
	private void addRows() {
		for(int i = 0; i < creations.size(); i++) {
			ImageView img = creations.get(i).getImage();
			
			Label name = new Label(creations.get(i).getName());
			Label search = new Label(creations.get(i).getSearch());
			Label guess = new Label(guesses.get(i));
			ImageView result;
			if(creations.get(i).getSearch().equals(guesses.get(i))) {
				result = loadImage(true);
			}else {
				result = loadImage(false);
			}
			
			Styling.textColorNotBold(name);
			Styling.textColorNotBold(guess);
			Styling.textColorNotBold(search);
			
			int fontSize = 20;
			
			search.setFont(Font.font ("Verdana", fontSize));
			name.setFont(Font.font ("Verdana", fontSize));
			guess.setFont(Font.font ("Verdana", fontSize));
			
			name.setAlignment(Pos.CENTER);
			guess.setAlignment(Pos.CENTER);
			search.setAlignment(Pos.CENTER);
			
			GridPane.setHalignment(search, HPos.CENTER);
			GridPane.setHalignment(guess, HPos.CENTER);
			GridPane.setHalignment(img, HPos.CENTER);
			GridPane.setHalignment(name, HPos.CENTER);
			GridPane.setHalignment(result, HPos.CENTER);
			
			layout.addRow(i+1, img,name, search, guess, result);
		}
	}
	
	/**
	 * Returns the image view for each result feedback icon
	 * @param result whether they got this creation correct or not
	 * @return an ImageView with the given result icon
	 */
	public ImageView loadImage(Boolean result) {
		// load the image
		File file;
		if(result) {
			file = new File("./resources/icons/tick.png");
		}else {
			file = new File("./resources/icons/cross.png");
		}
	    Image image = new Image(file.toURI().toString());

        // simple displays ImageView the image as is
        ImageView iv = new ImageView();
        iv.setImage(image);

        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(40);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        return iv;
	}
	
	/**
	 * Return screen with the feedback for the user
	 * @return feedback screen
	 */
	public BorderPane getScreen() {
		return panel;
	}
	
	/**
	 * Add labels to the column headers in the GridPane
	 */
	private void addLabels() {
		Label creation = new Label("Creation");
		Label search = new Label("Topic");
		Label guess = new Label("Your Guess");
		Label result = new Label("Result");
		result.setFont(Font.font ("Verdana", 25));

		creation.setFont(Font.font ("Verdana", 25));
		search.setFont(Font.font ("Verdana", 25));
		guess.setFont(Font.font ("Verdana", 25));
		result.setFont(Font.font ("Verdana", 25));

		result.setAlignment(Pos.CENTER);
		guess.setAlignment(Pos.CENTER);
		search.setAlignment(Pos.CENTER);
		creation.setAlignment(Pos.CENTER);
		
		Styling.textColor(result);
		Styling.textColor(guess);
		Styling.textColor(search);
		Styling.textColor(creation);
		
		GridPane.setHalignment(creation, HPos.CENTER);
		GridPane.setHalignment(search, HPos.CENTER);
		GridPane.setHalignment(guess, HPos.CENTER);
		GridPane.setHalignment(result, HPos.CENTER);
		
		layout.add(creation, 0, 0, 2, 1);
		layout.add(search, 2, 0);
		layout.add(guess, 3, 0);
		layout.add(result, 4, 0);
	}
}
