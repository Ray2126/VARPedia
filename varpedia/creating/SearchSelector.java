package varpedia.creating;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

//Screen where user enters search term
public class SearchSelector {
    VBox content;
    TextField searchInput;
    public SearchSelector(){
        content = new VBox();
        //content.setPadding(new Insets(10, 50, 50, 50));
        content.setSpacing(10);
        Text prompt = new Text("What would you like to search?");

        searchInput = new TextField();
        searchInput.setMaxWidth(250);

        content.getChildren().addAll(prompt, searchInput);
        content.setAlignment(Pos.CENTER);
    }

    public VBox invalidSearch(){
        VBox errorContent = new VBox();
        errorContent.setSpacing(10);
        Text prompt = new Text("What would you like to search?");
        Text errorPrompt = new Text("Cannot search on this term please try again");
        errorPrompt.setStyle("-fx-text-fill: #ff0000;");
        errorContent.getChildren().addAll(prompt, errorPrompt, searchInput);
        errorContent.setAlignment(Pos.CENTER);
        return errorContent;
    }

    public VBox getScene() {
        return content;
    }

    public String getInput()
    {
        return searchInput.getText();
    }
}
