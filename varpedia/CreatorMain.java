package varpedia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import varpedia.creating.ChunkEditorScreen;
import varpedia.creating.SearchSelector;

public class CreatorMain {
    // manages the screens for creating a new creation
	// Begins when create button clicked
	// beginCreate()      pop up this dialog
	// hi
    String searchedTerm;
    String creationName;
    int imageAmount;
    String currentScreen;
    BorderPane screenAndButtons;
    Scene screen;
    HBox nav;
    ChunkEditorScreen editor;
    Stage creationWindow;
    SearchSelector search;

    public CreatorMain(){
        search = new SearchSelector();
        editor = new ChunkEditorScreen();
        screenAndButtons = new BorderPane();
        creationWindow = new Stage();
        currentScreen = "search";
    }

    public void beginCreate() {
        addButtons();
        searchScreenUp();

        creationWindow.setTitle("Creation Maker");
        screen = new Scene(screenAndButtons, 1500, 1000);
        creationWindow.setScene(screen);
        creationWindow.show();
    }

    private void searchScreenUp(){
        screenAndButtons.setCenter(search.getScene());
    }

    private void loadCreateScreen(){
        searchedTerm = search.getInput();
        if(searchedTerm.isEmpty() || searchedTerm.isBlank()){
            invalidSearch();
            return;
        }
        System.out.println("searched: "+searchedTerm.length());
        editor.getTextSection().setSearched(searchedTerm, this);
    }

    public void invalidSearch(){
        screenAndButtons.setCenter(search.invalidSearch());
    }

    public void createScreenUp(){
        screenAndButtons.setCenter(editor.getScreen());
    }

    private void addButtons(){
        nav = new HBox();
        nav.setAlignment(Pos.BASELINE_RIGHT);

        Button back = new Button("Back");

        Button next = new Button("Next");
        next.setOnAction(e -> {
            loadCreateScreen();
        });

        Button cancel = new Button("Cancel");

        nav.setPadding(new Insets(10,10,10,10));
        nav.setSpacing(10);
        nav.getChildren().addAll(back, next, cancel);
        screenAndButtons.setBottom(nav);
    }


}
