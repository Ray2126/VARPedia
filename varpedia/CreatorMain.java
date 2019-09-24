package varpedia;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import varpedia.creating.SearchSelector;

public class CreatorMain {
    // manages the screens for creating a new creation
	// Begins when create button clicked
	// beginCreate()      pop up this dialog
	// hi
    String searchedTerm;
    String creationName;
    int imageAmount;
    BorderPane screenAndButtons;
    Scene screen;
    HBox nav;
    Stage creationWindow;
    SearchSelector search;

    public CreatorMain(){
        search = new SearchSelector();
        screenAndButtons = new BorderPane();
        creationWindow = new Stage();
    }

    public void beginCreate() {
        addButtons();
        searchScreenUp();

        creationWindow.setTitle("Creation Maker");
        screen = new Scene(screenAndButtons, 700, 500);
        creationWindow.setScene(screen);
        creationWindow.show();
    }

    private void searchScreenUp(){
        screenAndButtons.setCenter(search.getScene());
    }

    private void addButtons(){
        nav = new HBox();
        nav.setAlignment(Pos.BASELINE_RIGHT);

        Button back = new Button("Back");

        Button next = new Button("Next");
        next.setOnAction(e->{

        });

        Button cancel = new Button("Cancel");


        nav.getChildren().addAll(back, next, cancel);
        screenAndButtons.setBottom(nav);
    }
}
