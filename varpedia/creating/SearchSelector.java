package varpedia.creating;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//Screen where user enters search term
public class SearchSelector {
    VBox content;
    TextField searchInput;
    Text errorPrompt;
    Text prompt;
    public SearchSelector(){
        content = new VBox();
        //content.setPadding(new Insets(10, 50, 50, 50));
        content.setSpacing(10);
        prompt = new Text("What would you like to search?");
        prompt.setFont(Font.font(Font.getDefault().getName(),15));

        searchInput = new TextField();
        searchInput.setMaxWidth(250);
        content.getChildren().addAll(prompt, searchInput);
        content.setAlignment(Pos.CENTER);
    }

    public VBox invalidSearch(){
        VBox errorContent = new VBox();
        errorContent.setSpacing(10);
        errorPrompt = new Text("Cannot search on this term please try again");
        errorPrompt.setFill(Color.RED);
        errorContent.getChildren().addAll(prompt, errorPrompt, searchInput);
        errorContent.setAlignment(Pos.CENTER);
        return errorContent;
    }

    public VBox getScene() {
        content = new VBox();
        content.getChildren().addAll(prompt, searchInput);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        return content;
    }

    public String getInput()
    {
        return searchInput.getText();
    }
}
