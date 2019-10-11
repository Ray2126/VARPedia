package varpedia;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import varpedia.creating.*;

/**
 * Manages the screens for creating a new creation
 *
 */
public class CreatorMain {

	private BorderPane mainPane;
	private Scene scene;
	private Stage stage;
	
	private Home home;
	private SearchSelectorScreen searchScreen;
	private ChunkEditorScreen chunkScreen;
	private ImageSelectorScreen imagesScreen;
	private FinalPreviewScreen previewScreen;
	private String currentScreen;
	
	private HBox navBar;
	private Button backBtn;
	private Button nextBtn;
	private Button cancelBtn;
	private ProgressIndicator progressIndicator;
	
    private String searchedTerm;
    private int imageAmount;

    private Scripts scripts;
    
    
    public CreatorMain(Home home){
    	this.home = home;
        scripts = new Scripts();
        
        stage = new Stage();
        mainPane = new BorderPane();
        
        searchScreen = new SearchSelectorScreen();
        chunkScreen = new ChunkEditorScreen();
        imagesScreen = new ImageSelectorScreen();
        previewScreen = new FinalPreviewScreen();
        
        //Initialize nav bar
        navBar = new HBox();
        backBtn = new Button("Back");
        backBtn.setOnAction(e -> backButtonClicked());
        nextBtn = new Button("Next");
        nextBtn.setOnAction(e -> nextButtonClicked());
        cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> closeRequest());
        progressIndicator = new ProgressIndicator();
        progressIndicator.isIndeterminate();
        
        //Navbar style
        navBar.setPadding(new Insets(10,10,0,10));
        navBar.setSpacing(10);
        navBar.setAlignment(Pos.CENTER_RIGHT);
        progressIndicator.setOpacity(0);
        progressIndicator.setMaxWidth(20);
        
        navBar.getChildren().addAll(progressIndicator, backBtn, nextBtn, cancelBtn);
        mainPane.setBottom(navBar);
    }

    /**
     * Confirm closing with popup
     */
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

    /**
     * Close creation window
     * Have to clean up all temp created files
     */
    public void close(){
        //cleanup files
        scripts.getScript("cleanup", new String[]{});
        stage.close();
        home.showHome();
        
        //Stop video player if on preview screen
        previewScreen.stop();
    }
    
    /**
     * Initial screen up when entering create mode
     */
    public void beginCreate() {
        searchScreenUp();

        stage.setTitle("Creation Maker");
        scene = new Scene(mainPane, 1500, 1000);
        stage.setScene(scene);
        
        //When top right exit pressed does this
        stage.setOnCloseRequest(e -> {
        	closeRequest();
        	e.consume();
        });
        stage.show();
    }

    /**
     * Set screen center to the search screen
     */
    private void searchScreenUp(){
        currentScreen = "search";
        mainPane.setCenter(searchScreen.getScene());
        backBtn.setDisable(true);
    }

    /**
     * Set up the chunk screen and search for word
     */
    private void loadChunkScreen(){
    	showProgressIndicator();
        searchedTerm = searchScreen.getInput();
        
        if(searchedTerm.isEmpty()){
            invalidSearch();
            hideProgressIndicator();
            return;
        }
        searchForTerm();
        chunkScreen.getTextSection().setSearched(searchedTerm);
    }

    /**
     * Search for the term user inputted in the searchSelectorScreen
     */
    private void searchForTerm() {
		SearchTask task = new SearchTask(searchedTerm);

		task.setOnSucceeded(e -> {
			TextArea dummy = task.getValue();
			if(dummy == null) {
				invalidSearch();
				hideProgressIndicator();
			}else {
				chunkScreen.getTextSection().setTextArea(dummy);
				chunkScreenUp();
				hideProgressIndicator();
			}
		});
		
		try {
			ExecutorService ex = Executors.newSingleThreadExecutor();
			ex.submit(task);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    
    /**
     * When the searched word is invalid
     */
	private void invalidSearch(){
        mainPane.setCenter(searchScreen.invalidSearch());
    }

    /**
     * Set the screen to the chunk screen
     */
    private void chunkScreenUp(){
        backBtn.setDisable(false);
        mainPane.setCenter(chunkScreen.getScreen());
        currentScreen="create";
    }

	/**
	 * Set up image screen and get images from Flickr
	 */
	private void loadImageScreen(){
		showProgressIndicator();
		
		//No chunks created
        if(! chunkScreen.anySelected()){
        	hideProgressIndicator();
            return;
        }
        
        GetImageTask task = new GetImageTask(searchedTerm);
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.submit(task);
        
        task.setOnSucceeded(e -> {
        	imagesScreen.loadImages();
            imageScreenUp();
            hideProgressIndicator();
        });
	}
	
	/**
	 * Set screen to the imageSelectorScreen
	 */
    private void imageScreenUp(){
        currentScreen="image";
        mainPane.setCenter(imagesScreen);
    }
    

    public int getImageAmount() {
        imagesScreen.saveSelectedImages();
        imageAmount = imagesScreen.getAmountSelected();
        return imageAmount;
    }
	
    /**
     * Set up final preview screen and combine video and audio
     */
    private void loadPreviewScreen(){
        //make sure images selected
        if(imagesScreen.isSelected()) {
        	showProgressIndicator();
            chunkScreen.combineTheAudio(this);
        }
    }
    
    /**
     * Set screen to final preview screen
     */
    public void previewScreenUp(){
        previewScreen.playVideo(new Creation("preview", 0));
        mainPane.setCenter(previewScreen.getScreen());
        nextBtn.setText("Finish");
        currentScreen="preview";
        hideProgressIndicator();
    }
    
    public FinalPreviewScreen getPreview() {
    	previewScreen = new FinalPreviewScreen();
        return previewScreen;
    }

    private void backButtonClicked(){
    	hideProgressIndicator();
        if(currentScreen == "preview"){
            mainPane.setCenter(imagesScreen);
            currentScreen="image";
            nextBtn.setText("Next");
            previewScreen.stop();
        }
        else if(currentScreen=="image"){
            mainPane.setCenter(chunkScreen.getScreen());
            currentScreen="create";
        }
        else if(currentScreen == "create"){
            searchScreenUp();
            currentScreen="search";
        }
    }

    private void nextButtonClicked(){
        if(currentScreen == "search"){
            loadChunkScreen();
        }else if(currentScreen == "create") {
            loadImageScreen();
        }else if(currentScreen == "image"){
            loadPreviewScreen();
        }else if(currentScreen == "preview"){
            previewScreen.createNew(this);
        }
    }
    
    private void hideProgressIndicator(){
    	progressIndicator.setOpacity(0);
    }

    private void showProgressIndicator(){
    	progressIndicator.setOpacity(100);
    }
}
