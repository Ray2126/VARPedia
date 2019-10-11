package varpedia;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class FeedbackScreen {
	private GridPane layout;
	private StackPane panel;
	private List<String> creations;
	private List<String> guesses;
	private List<String> actual;
	
	public FeedbackScreen(List<String> creations, List<String> guesses, List<String> actual) {
		layout = new GridPane();
		labels();
		
		panel = new StackPane();
		panel.getChildren().add(layout);
		
		this.creations = creations;
		this.guesses = guesses;
		this.actual = actual;
		
		addRows();
	}
	
	/**
	 * Recursively add feedback for each video
	 */
	private void addRows() {
		for(int i = 0; i < creations.size(); i++) {
			Label creation = new Label(creations.get(i));
			Label search = new Label(actual.get(i));
			Label guess = new Label(guesses.get(i));
			Label result = new Label("Result");
			layout.addRow(i+1, creation, search, guess, result);
		}
	}
	
	/**
	 * Add labels to the column headers in the GridPane
	 */
	private void labels() {
		Label creation = new Label("Creation Name");
		Label search = new Label("Actual Search Term");
		Label guess = new Label("Your Guess");
		Label result = new Label("Result");
		layout.addRow(0, creation, search, guess, result);
	}
}
