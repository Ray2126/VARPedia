package varpedia.create;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.helper.Styling;

//Screen where user enters search term
public class SearchSelectorScreen extends HBox{
	
    private TextField searchInput;
    private Text prompt;
    
    public SearchSelectorScreen(){
        prompt = new Text("Search: ");
        prompt.setFont(Font.font ("Verdana", 20));
        Styling.textColor(prompt);

        searchInput = new TextField();
        searchInput.setMinWidth(320);
        searchInput.setMaxWidth(320);
        Styling.textField(searchInput);
        
        Styling.yellowBG(this);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(prompt, searchInput);
        
        //Auto focus to searchInput
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				searchInput.requestFocus();
			}
        });
    }

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
