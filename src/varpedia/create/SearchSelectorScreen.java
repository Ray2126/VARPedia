package varpedia.create;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import varpedia.helper.Styling;

/**
 * Screen where user enters search term
 *
 */
public class SearchSelectorScreen extends HBox{

    private TextField searchInput;
    

    public SearchSelectorScreen(){
        Styling.yellowBG(this);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        setUpPrompt();
        setUpTextField();
    }
    
    /**
     * Set up the label - "search:"
     */
    private void setUpPrompt() {
    	Label prompt = new Label("Search: ");
        prompt.setFont(Font.font ("Verdana", 20));
        Styling.textColor(prompt);
        this.getChildren().add(prompt);
    }
    
    private void setUpTextField() {
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

    public String getInput() {
        return searchInput.getText();
    }
}
