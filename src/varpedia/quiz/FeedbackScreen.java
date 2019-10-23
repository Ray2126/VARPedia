package varpedia.quiz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import varpedia.helper.LoadIcon;
import varpedia.home.Home;

public class FeedbackScreen {
	private VBox screen;
	private GridPane layout;
	private BorderPane panel;
	private List<String> creations;
	private List<String> guesses;
	private List<String> actual;
	private Home home;
	
	private HBox navBar;
	private Button homeBtn;
	
	public FeedbackScreen(List<String> creations, List<String> guesses, List<String> actual, Home home) {
		this.home = home;
		layout = new GridPane();
		layout.setPadding(new Insets(20,20,20,20));
		labels();
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(50);
		layout.setVgap(50);
		
		panel = new BorderPane();
		BorderPane.setAlignment(layout, Pos.CENTER);
		panel.setCenter(layout);
		
		this.creations = creations;
		this.guesses = guesses;
		this.actual = actual;
		
		addRows();
		
		
		 //Initialize nav bar
        navBar = new HBox();
        homeBtn = new Button();
        homeBtn.setGraphic(LoadIcon.loadIcon("home", 30, 30));
        homeBtn.setDefaultButton(true);
        homeBtn.setFont(Font.font ("Verdana", 15));
        homeBtn.setOnAction(e -> {
        	home.showHome();
        });

        
        //Navbar style
        navBar.setPadding(new Insets(10,10,10,10));
        navBar.setSpacing(10);
        navBar.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setAlignment(navBar, Pos.CENTER_RIGHT);
        
        navBar.getChildren().addAll(homeBtn);
        panel.setBottom(navBar);
	}
	
	/**
	 * Recursively add feedback for each video
	 */
	private void addRows() {
		for(int i = 0; i < creations.size(); i++) {
			Label creation = new Label(creations.get(i));
			creation.setAlignment(Pos.CENTER);
			creation.setFont(Font.font ("Verdana", 20));
			GridPane.setHalignment(creation, HPos.CENTER);
			Label search = new Label(actual.get(i));
			search.setAlignment(Pos.CENTER);
			search.setFont(Font.font ("Verdana", 20));
			GridPane.setHalignment(search, HPos.CENTER);
			Label guess = new Label(guesses.get(i));
			guess.setAlignment(Pos.CENTER);
			guess.setFont(Font.font ("Verdana", 20));
			GridPane.setHalignment(guess, HPos.CENTER);
			Label result;
			if(actual.get(i).equals(guesses.get(i))) {
				result = new Label("Correct!");
			}else {
				result = new Label("Incorrect!");
			}
			result.setAlignment(Pos.CENTER);
			result.setFont(Font.font ("Verdana", 20));
			GridPane.setHalignment(result, HPos.CENTER);
			layout.addRow(i+1, creation, search, guess, result);
		}
	}
	
	/**
	 * Return screen
	 * @return
	 */
	public BorderPane getScreen() {
		return panel;
	}
	
	/**
	 * Add labels to the column headers in the GridPane
	 */
	private void labels() {
		Label creation = new Label("Creation Name");
		creation.setAlignment(Pos.CENTER);
		layout.setFillWidth(creation, true);
		creation.setFont(Font.font ("Verdana", 25));
		Label search = new Label("Actual Search Term");
		search.setAlignment(Pos.CENTER);
		search.setFont(Font.font ("Verdana", 25));
		Label guess = new Label("Your Guess");
		guess.setAlignment(Pos.CENTER);
		guess.setFont(Font.font ("Verdana", 25));
		Label result = new Label("Result");
		result.setAlignment(Pos.CENTER);
		result.setFont(Font.font ("Verdana", 25));
		layout.addRow(0, creation, search, guess, result);
	}
}
