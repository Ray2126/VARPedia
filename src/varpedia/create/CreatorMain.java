package varpedia.create;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import varpedia.helper.GetImageTask;
import varpedia.helper.Scripts;
import varpedia.helper.SearchTask;
import varpedia.helper.Styling;
import varpedia.home.Home;

/**
 * Manages the screens for creating a new creation
 *
 */
public class CreatorMain {

	/**
	 * The main pane containing the buttons and the current screen.
	 */
	private BorderPane mainPane;
	
	/**
	 * The pane containing the progress indicator.
	 */
	private BorderPane loadingPane;
	
	/**
	 * The stack pane that will have the loading pane added on once task is running.
	 */
	private StackPane stackPane;
	
	/**
	 * The creating stage.
	 */
	private Stage stage;
	
	/**
	 * The progress indicator shown when tasks are running.
	 */
	private ProgressIndicator progressIndicator;
	private Home home;
	private SearchSelectorScreen searchScreen;
	private ChunkEditorScreen chunkScreen;
	private MusicSelectorScreen musicScreen;
	private ImageSelectorScreen imagesScreen;
	private FinalPreviewScreen previewScreen;
	
	/**
	 * A string that contains a description of the current screen.
	 */
	private String currentScreen;
	
	/**
	 * The navigation bar pane that will have back, next and cancel buttons.
	 */
	private HBox navBar;
	private Button backBtn;
	private Button nextBtn;
	private Button cancelBtn;
    private String searchedTerm;

    /**
     * Linux scripts.
     */
    private Scripts scripts;
    
    /**
     * Constructor.
     * @param home	the home screen
     */
    public CreatorMain(Home home){
    	this.home = home;
        scripts = new Scripts();
        stage = new Stage();
        
        stackPane = new StackPane();
        mainPane = new BorderPane();
        loadingPane = new BorderPane();
        
        searchScreen = new SearchSelectorScreen();
        chunkScreen = new ChunkEditorScreen();
        musicScreen = new MusicSelectorScreen();
        imagesScreen = new ImageSelectorScreen();
        
        progressIndicator = new ProgressIndicator();
        
        navBar = new HBox();
        backBtn = new Button("Back");
        nextBtn = new Button("Next");
        cancelBtn = new Button("Cancel");
        
        initStage();
    	initialiseNavBar();
    	initialiseProgressIndicator();
    
        mainPane.setBottom(navBar);
        stackPane.getChildren().add(mainPane);
    }

    /**
     * Initialize the nav bar (back, next, cancel buttons).
     */
	private void initialiseNavBar() {
        Styling.blueButton(backBtn);
        Styling.blueButton(nextBtn);
        Styling.blueButton(cancelBtn);
        
        backBtn.setFont(Font.font ("Verdana", 15));
        nextBtn.setFont(Font.font ("Verdana", 15));
        cancelBtn.setFont(Font.font ("Verdana", 15));
        
        backBtn.setOnAction(e -> backButtonClicked());
        nextBtn.setOnAction(e -> nextButtonClicked());
        cancelBtn.setOnAction(e -> closeRequest());
        
        nextBtn.setDefaultButton(true);

        navBar.setPadding(new Insets(10,10,10,10));
        navBar.setSpacing(10);
        navBar.setAlignment(Pos.CENTER_RIGHT);
        Styling.yellowBG(navBar);
        
        navBar.getChildren().addAll(backBtn, nextBtn, cancelBtn);
    }
	
	/**
	 * Initialize the progressIndicator which will show once background tasks are running.
	 */
    private void initialiseProgressIndicator() {
        progressIndicator.isIndeterminate();
        loadingPane.setCenter(progressIndicator);
        progressIndicator.setPrefWidth(2000);
	}
    
    /**
     * Confirm closing creation process with pop-up.
     */
    private void closeRequest() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Are you sure?");
    	alert.setHeaderText("Are you sure you want to cancel this creation?");
    	alert.setContentText("You will lose all saved data.");
    	
    	DialogPane dialogPane = alert.getDialogPane();
    	Styling.dialogStyle(dialogPane);

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
     * Close creation window.
     * Have to clean up all temp created files.
     */
    public void close(){
        //cleanup files
        scripts.getScript("cleanup", new String[]{});
        stage.close();
        home.showHome();
        
        //Stop video player if on preview screen
        if(currentScreen.equals("preview")) {
            previewScreen.stopVideo();
        }
    }
    
    /**
     * Initialize Stage  
     */
    private void initStage() {
        stage.setTitle("Creation Maker");
        Scene scene = new Scene(stackPane, 1200, 815);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("VARpedia Create");
        
        //When top right exit pressed does this
        stage.setOnCloseRequest(e -> {
        	closeRequest();
        	e.consume();
        });
    }
    
    /**
     * Initial screen up when entering create mode.
     */
    public void beginCreate() {
        searchScreenUp();
        stage.show();
    }

    /**
     * Set screen center to the search screen.
     */
    private void searchScreenUp(){
        currentScreen = "search";
        mainPane.setCenter(searchScreen);
        backBtn.setDisable(true);
    }

    /**
     * Set up the chunk screen and search for word.
     */
    private void loadChunkScreen(){
    	showProgressIndicator();
        searchedTerm = searchScreen.getInput();
        searchedTerm.trim();
        searchedTerm.toLowerCase();
        if(searchedTerm.isEmpty() || searchedTerm.contains(" ")){
            searchScreen.invalidSearch();
            hideProgressIndicator();
            return;
        }
        searchForTerm();
        chunkScreen.getTextSection().setUp(searchedTerm);
    }

    /**
     * Search for the term user inputted in the searchSelectorScreen.
     */
    private void searchForTerm() {
		SearchTask task = new SearchTask(searchedTerm);

		task.setOnSucceeded(e -> {
			TextArea dummy = task.getValue();
			if(dummy == null) {
				searchScreen.invalidSearch();
				hideProgressIndicator();
			}else {
				chunkScreen.getTextSection().searchComplete(dummy);
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
     * Set the screen to the chunk screen.
     */
    private void chunkScreenUp(){
        backBtn.setDisable(false);
        mainPane.setCenter(chunkScreen.getScreen());
        currentScreen="create";
    }
    
    /**
     * Before music screen, check they have at least one chunk.
     */
    private void loadMusicScreen() {
        if(! chunkScreen.anyChunksCreated()){
        	hideProgressIndicator();
        } else {
        	musicScreenUp();
        }
    }
    
    /**
     * Set music screen to center.
     */
    private void musicScreenUp() {
    	currentScreen = "music";
    	mainPane.setCenter(musicScreen.getScreen());
    }

	/**
	 * Set up image screen and get images from Flickr.
	 */
	private void loadImageScreen(){
		showProgressIndicator();
		
        GetImageTask task = new GetImageTask(searchedTerm);
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.submit(task);
        
        task.setOnSucceeded(e -> {
        	imagesScreen.setUp();
            imageScreenUp();
            hideProgressIndicator();
        });
        
	}
	
	/**
	 * Set screen to the choosing images screen.
	 */
    private void imageScreenUp(){
        currentScreen="image";
        mainPane.setCenter(imagesScreen.getScreen());
    }
    
    /**
     * Get the number of images that have been selected on the image screen.
     * @return	number of images selected
     */
    public int getImageAmount() {
        imagesScreen.saveSelectedImages();
        return imagesScreen.getAmountSelected();
    }
	
    /**
     * Set up final preview screen and combine video and audio.
     */
    private void loadPreviewScreen(){
        //make sure images selected
        if(imagesScreen.isSelected()) {
        	showProgressIndicator();
            chunkScreen.combineTheAudio(this, musicScreen.getSelectedMusic().getName());
        }
    }
    
    /**
     * Set screen to final preview screen.
     */
    public void previewScreenUp(){
        previewScreen.playVideo();
        mainPane.setCenter(previewScreen.getScreen());
        nextBtn.setText("Finish");
        currentScreen="preview";
        hideProgressIndicator();
    }
    
    /**
     * Get the preview screen.
     * @return	the preview screen
     */
    public FinalPreviewScreen getPreview() {
    	previewScreen = new FinalPreviewScreen(searchScreen.getInput());
        return previewScreen;
    }

    /**
     * Nav bar back button is clicked.
     */
    private void backButtonClicked(){
    	hideProgressIndicator();
        if(currentScreen == "preview"){
            imageScreenUp();
            nextBtn.setText("Next");
            previewScreen.stopVideo();
        }
        else if(currentScreen=="image"){
            musicScreenUp();
        }
        else if(currentScreen == "music") {
        	chunkScreenUp();
        }
        else if(currentScreen == "create"){
        	//remove the text files
            searchScreenUp();
        }
    }

    /**
     * Nav bar next button is clicked.
     */
    private void nextButtonClicked(){
        if(currentScreen == "search"){
            loadChunkScreen();
        }else if(currentScreen == "create") {
        	loadMusicScreen();
        }else if(currentScreen == "music") {
        	loadImageScreen();
        }else if(currentScreen == "image"){
            loadPreviewScreen();
        }else if(currentScreen == "preview"){
            if(previewScreen.isValidName()) {
            	previewScreen.createCreation(this);
            }
        }
    }
    
    /**
     * Show progress indicator while lengthy tasks are running in background.
     */
    private void showProgressIndicator(){
    	stackPane.getChildren().add(loadingPane);
    	mainPane.setOpacity(0.5);
    	
    }
    
    /**
     * Hide the progress indicator after loading is complete.
     */
    private void hideProgressIndicator(){
    	stackPane.getChildren().remove(loadingPane);
    	mainPane.setOpacity(100);
    }


}
