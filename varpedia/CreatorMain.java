package varpedia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import varpedia.creating.ChunkEditorScreen;
import varpedia.creating.FinalPreview;
import varpedia.creating.GetImageTask;
import varpedia.creating.SearchSelector;

public class CreatorMain {
    // manages the screens for creating a new creation
	// Begins when create button clicked
	// beginCreate()      pop up this dialog
	// hi
    String searchedTerm;
    String creationName;
    int imageAmount;
    Button next;
    String currentScreen;
    BorderPane screenAndButtons;
    Scene screen;
    HBox nav;
    ChunkEditorScreen editor;
    FinalPreview preview;
    Stage creationWindow;
    SearchSelector search;
    Button back;

    public CreatorMain(){
        search = new SearchSelector();
        editor = new ChunkEditorScreen();
        screenAndButtons = new BorderPane();
        creationWindow = new Stage();
        currentScreen = "search";
        preview = new FinalPreview();
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
        back.setDisable(true);
    }

    private void loadCreateScreen(){
        searchedTerm = search.getInput();
        if(searchedTerm.isEmpty()){
            invalidSearch();
            return;
        }
        editor.getTextSection().setSearched(searchedTerm, this);
    }

    public void invalidSearch(){
        screenAndButtons.setCenter(search.invalidSearch());
    }

    public void createScreenUp(){
        back.setDisable(false);
        screenAndButtons.setCenter(editor.getScreen());
        currentScreen="create";
        GetImageTask task = new GetImageTask(searchedTerm);
        task.run();
        next.setText("Next");

    }

    private void loadPreviewScreen(){
        //create audio file
        editor.combineTheAudio(this);
        //get audio length
        //create image file

    }

    public int getImageAmount() {
        //imageSelector get image amount
        return imageAmount;
    }

    public FinalPreview getPreview() {
        return preview;
    }

    public void previewScreenUp(){
        preview.playVideo(new Creation("preview", 0));
        screenAndButtons.setCenter(preview.getScreen());
        next.setText("Finish");
        currentScreen="preview";
    }

    private void addButtons(){
        nav = new HBox();
        nav.setAlignment(Pos.BASELINE_RIGHT);

        back = new Button("Back");
        back.setOnAction(e -> backButtonClicked());

        next = new Button("Next");
        next.setOnAction(e -> {
            nextButtonClicked();
        });

        Button cancel = new Button("Cancel");

        nav.setPadding(new Insets(10,10,10,10));
        nav.setSpacing(10);
        nav.getChildren().addAll(back, next, cancel);
        screenAndButtons.setBottom(nav);
    }

    private void close(){

    }

    private void backButtonClicked(){
        if(currentScreen == "preview"){
            screenAndButtons.setCenter(editor.getScreen());
            currentScreen="create";
            next.setText("Next");
            next.setDisable(false);
        }else if(currentScreen == "create"){
            searchScreenUp();
            currentScreen="search";
        }
    }

    private void nextButtonClicked(){
        if(currentScreen == "search"){
            loadCreateScreen();
            return;
        }else if(currentScreen == "create"){
            //Should be image select
            //Right now make audio combined
            loadPreviewScreen();
        }else if(currentScreen == "preview"){
            //clean up files
            close();
        }
    }

}
