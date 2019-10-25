package varpedia.create;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.helper.Styling;

/**
 * Screen where user enters search term
 *
 */
public class SearchSelectorScreen extends HBox{
	
	/**
	 * Text field where user enters term
	 */
    private TextField searchInput;
    
    /**
     * Constructor
     */
    public SearchSelectorScreen(){
        Styling.yellowBG(this);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        setUpPrompt();
        setUpSearchInput();
    }
    
    /**
     * Set up the label - "search:"
     */
    private void setUpPrompt() {
    	Text prompt = new Text("Search: ");
        prompt.setFont(Font.font ("Verdana", 20));
        Styling.textColor(prompt);
        this.getChildren().add(prompt);
    }
    
    /**
     * Set up the text field
     */
    private void setUpSearchInput() {
    	searchInput = new TextField();
        searchInput.setMinWidth(320);
        searchInput.setMaxWidth(320);
        Styling.textField(searchInput);
        this.getChildren().add(searchInput);
        //Auto focus to searchInput
        Platform.runLater(new Runnable() {
        	@Override public void run() { searchInput.requestFocus(); }
        });
    }

    /**
     * Search is invalid, set text field red
     */
    public void invalidSearch(){
    	Styling.errorSearch(searchInput);
        searchInput.setOnMouseClicked(e-> {
        	Styling.textField(searchInput);
        });
    }

    /**
     * Get the term the user searched
     * @return	term the user searched
     */
    public String getInput() {
        return searchInput.getText();
    }
}
