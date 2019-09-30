package varpedia;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import varpedia.creating.*;

/**
 * Manages the screens for creating a new creation
 *
 */
public class CreatorMain {

    private CreationTable tableParent;
    private String searchedTerm;
    private String creationName;
    private int imageAmount;
    private Button next;
    private String currentScreen;
    private BorderPane screenAndButtons;
    private Scene screen;
    private HBox nav;
    private ChunkEditorScreen editor;
    private ImageSelector images;
    private FinalPreview preview;
    private Stage creationWindow;
    private SearchSelector search;
    private Button back;
    private Scripts scripts;
    private HBox loadingNav;
    private ProgressIndicator prog;
    private Button cancel;
    private Text load;

    public CreatorMain(CreationTable parent){
        scripts = new Scripts();
        scripts.getScript("cleanup", new String[]{});
        this.tableParent = parent;
        search = new SearchSelector();
        editor = new ChunkEditorScreen();
        screenAndButtons = new BorderPane();
        creationWindow = new Stage();
        currentScreen = "search";
        preview = new FinalPreview();
        prog = new ProgressIndicator();
        prog.isIndeterminate();
        loadingNav = new HBox();
    }

    public void beginCreate() {
        addButtons();
        searchScreenUp();

        creationWindow.setTitle("Creation Maker");
        screen = new Scene(screenAndButtons, 1500, 1000);
        creationWindow.setScene(screen);
        creationWindow.setOnCloseRequest(e -> {
        	closeRequest();
        	e.consume();
        });
        creationWindow.show();
    }
    
    private void closeRequest() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Are you sure?");
    	alert.setHeaderText("Are you sure you want to cancel this creation?");
    	alert.setContentText("You will lose all saved data.");

    	ButtonType buttonTypeTwo = new ButtonType("Continue Creating");
    	ButtonType buttonTypeOne = new ButtonType("Exit");

    	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == buttonTypeOne){
    	    close();
    	} else{
    	    return;
    	}
    }

    private void searchScreenUp(){
        screenAndButtons.setCenter(search.getScene());
        back.setDisable(true);
    }

    private void loadCreateScreen(){
    	loadingButtons();
        screenAndButtons.setCenter(search.getScene());
        searchedTerm = search.getInput();
        
        if(searchedTerm.isEmpty()){
            invalidSearch();
            loadingButtons();
            return;
        }
        editor.getTextSection().setSearched(searchedTerm, this);
        normalButtons();
    }

    public void invalidSearch(){
        screenAndButtons.setCenter(search.invalidSearch());
        loadingButtons();
    }


    public void createScreenUp(){
    	loadingButtons();
        back.setDisable(false);
        screenAndButtons.setCenter(editor.getScreen());
        currentScreen="create";
        next.setText("Next");

    }

    private void loadPreviewScreen(){
    	
        //create audio file

        //make sure images selected
        if(images.isSelected()) {
        	normalButtons();
            editor.combineTheAudio(this);
        }
        //get audio length
        //create image file
    }

    public int getImageAmount() {
        //imageSelector get image amount
        images.saveSelectedImages();
        imageAmount = images.getAmountSelected();
        return imageAmount;
    }

    private void loadImageScreen(){
        if(! editor.anySelected()){
        	loadingButtons();
            return;
        }
        GetImageTask task = new GetImageTask(searchedTerm);
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.submit(task);
        task.setOnSucceeded(e -> {
            imageScreenUp();
        });
        normalButtons();
    }

    public void imageScreenUp(){
    	loadingButtons();
        currentScreen="image";
        images = new ImageSelector();
        screenAndButtons.setCenter(images);
    }

    public FinalPreview getPreview() {
    	preview = new FinalPreview();
        return preview;
    }

    public void previewScreenUp(){
        preview.playVideo(new Creation("preview", 0));
        screenAndButtons.setCenter(preview.getScreen());
        next.setText("Finish");
        currentScreen="preview";
        loadingButtons();
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

        cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
        	closeRequest();
        });

        nav.setPadding(new Insets(10,10,0,10));
        nav.setSpacing(10);
        nav.setAlignment(Pos.CENTER_RIGHT);
        //Loading . . .
        load = new Text("");
        load.setFill(Color.RED);
        prog.setOpacity(0);
        prog.setMaxWidth(20);
        nav.getChildren().addAll(prog, back, next, cancel);
        screenAndButtons.setBottom(nav);
    }

    private void loadingButtons(){
    	prog.setOpacity(0);
    }

    private void normalButtons(){
    	//prog.setOpacity(0);
    	prog.setOpacity(100);
    }

    public void close(){
        //cleanup files
        scripts.getScript("cleanup", new String[]{});
        creationWindow.close();
        tableParent.loadCreations();
        preview.stop();
    }

    private void backButtonClicked(){
    	loadingButtons();
        if(currentScreen == "preview"){
            screenAndButtons.setCenter(images);
            currentScreen="image";
            next.setText("Next");
            next.setDisable(false);
            preview.stop();
        }else if(currentScreen == "create"){
            searchScreenUp();
            currentScreen="search";
        }else if(currentScreen=="image"){
            screenAndButtons.setCenter(editor.getScreen());
            currentScreen="create";
            next.setText("Next");
            next.setDisable(false);
        }
    }

    private void nextButtonClicked(){
        if(currentScreen == "search"){
            loadCreateScreen();
        }else if(currentScreen == "create") {
            //Should be image select
            //Right now make audio combined
            loadImageScreen();
        }else if(currentScreen == "image"){
            loadPreviewScreen();
        }else if(currentScreen == "preview"){
            preview.createNew(this);
            //clean up files
        }
    }

}
