package varpedia.quiz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import varpedia.helper.LoadIcon;
import varpedia.helper.Styling;
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
		
        ScrollPane scroll = new ScrollPane();
        Styling.yellowBG(scroll);
        
		layout = new GridPane();
		layout.setPadding(new Insets(20,20,20,20));
		labels();
		Styling.yellowBG(layout);
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(20);
		layout.setVgap(20);
		
		
		panel = new BorderPane();
		panel.setTop(layout);
		Styling.yellowBG(panel);
		layout.setMaxWidth(781);
		
		this.creations = creations;
		this.guesses = guesses;
		this.actual = actual;
		
		addRows();
		
		
		 //Initialize nav bar
        navBar = new HBox();
        homeBtn = new Button();
        Styling.blueButton(homeBtn);
        homeBtn.setGraphic(LoadIcon.loadIcon("home", 30, 30));
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
        panel.setPrefHeight(773);
        panel.setBottom(navBar);
	}
	
	/**
	 * Recursively add feedback for each video
	 */
	private void addRows() {
		for(int i = 0; i < creations.size(); i++) {
			HBox creation = new HBox();
			creation.setAlignment(Pos.CENTER);
			creation.setSpacing(10);
			ImageView img = getImageView(creations.get(i));
			Label name = new Label(creations.get(i));
			name.setAlignment(Pos.CENTER);
			name.setFont(Font.font ("Verdana", 20));
			creation.getChildren().addAll(img, name);
			
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
	
	private ImageView getImageView(String creation) {
		// load the image
	    File file = new File("./creations/"+creation+"/thumb.jpg");
	    Image image = new Image(file.toURI().toString());

        // simple displays ImageView the image as is
        ImageView iv = new ImageView();
        iv.setImage(image);

        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(80);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        return iv;
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
		Label creation = new Label("Creation");
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
