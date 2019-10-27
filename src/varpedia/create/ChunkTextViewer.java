package varpedia.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.components.StopPlayButton;
import varpedia.helper.LoadIcon;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

import java.io.File;
import java.io.IOException;

/**
 * Top half of the chunk screen. Contains text area to edit, play and save chunks, and voice options.
 *
 */
public class ChunkTextViewer{
	
	BorderPane screen;
	/**
	 * The table of chunks this is associated to.
	 */
	private ChunkTable chunkTable;
	
	/**
	 * The text area where user can edit and select text.
	 */
	private TextArea textArea;
	
	/**
	 * The pane that contains the play and delete buttons and the voice icon and combo box.
	 */
	private HBox buttonVoicePane;
	
	/**
	 * The play button.
	 */
	private StopPlayButton playBtn;
	
	/**
	 * The save button.
	 */
	private Button saveBtn;
	
	/**
	 * The combo box of voices.
	 */
	private ComboBox<String> voices;
	
	/**
	 * The text that displays error if selected text is not valid.
	 */
	private Text error;
	
	/**
	 * The number of chunks the user has saved.
	 */
	private int savedChunks;
	
	/**
	 * Linux scripts.
	 */
	private Scripts scripts;
	
	
	/**
	 * Constructor.
	 * @param chunkTable	the chunk table this will be associated with
	 */
	public ChunkTextViewer(ChunkTable chunkTable) {
	    this.chunkTable = chunkTable;
	    scripts = new Scripts();
	    screen = new BorderPane();
	    buttonVoicePane = new HBox();
	    playBtn = new StopPlayButton(30,30);
	    saveBtn = new Button();
	    
	    screen.setMaxWidth(1100);
    }
	
	public BorderPane getScreen() {
		return screen;
	}
	
	/**
	 * Set up this screen .
	 * @param searched  the term the user searched
	 */
    public void setUp(String searched){
    	setTitle(searched);
		setUpButtons();
    }
    
    /**
     * Set up the title showing what they searched on top of this screen.
     * @param searched	the term the user searched
     */
    private void setTitle(String searched) {
    	searched = searched.toUpperCase();
    	Label searchTerm = new Label(searched);
		searchTerm.setFont(Font.font(Font.getDefault().getName(),26));
		Styling.textColor(searchTerm);
		
    	HBox titlePane = new HBox();
		titlePane.setPadding(new Insets(10,10,10,30));
		titlePane.getChildren().add(searchTerm);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        
        screen.setTop(titlePane);
    }

    /**
     * Set up pane for the buttons, voices options and error text.
     */
    private void setUpButtons(){
    	buttonVoicePane.setSpacing(10);
		buttonVoicePane.setAlignment(Pos.CENTER);
		buttonVoicePane.setPadding(new Insets(10));
		
    	addButtons();
		loadVoicesBox();
		addVoiceOptions();
		
		screen.setBottom(addErrorText());
	}
    
    /**
     * Add buttons to the pane.
     */
    private void addButtons() {
		playBtn.setOnAction(e -> playClicked());

		saveBtn.setGraphic(LoadIcon.loadIcon("save", 30, 30));
		saveBtn.setOnAction(e -> saveClicked());
		
		Styling.blueButton(saveBtn);
		Styling.blueButton(playBtn);
		
		//Centre the buttons
    	Region region = new Region();
		region.setMaxWidth(450);
		region.setMinWidth(450);
		buttonVoicePane.getChildren().addAll(region, playBtn, saveBtn);
    }

    /**
     * Load the combo box used for choosing voice.
     */
    private void loadVoicesBox(){
		ObservableList<String> voiceOptions =
				FXCollections.observableArrayList(
						"American Accent",
						"New Zealand Accent",
						"Robot Voice"
				);
		
		voices = new ComboBox<String>(voiceOptions);
		
		voices.setMinWidth(230);
		voices.setStyle("-fx-font: 16px \"Verdana\";");
		voices.getSelectionModel().selectFirst();
		
		Styling.comboBox(voices);
	}
    
    /**
     * Add the combo box and icon to the pane.
     */
    private void addVoiceOptions() {
    	Label voiceIcon = new Label();
		Region region = new Region();
		
		voiceIcon.setGraphic(LoadIcon.loadIcon("voice", 30, 30));
		
		//Anchor voices to right
		HBox.setHgrow(region, Priority.ALWAYS);
		buttonVoicePane.getChildren().addAll(region,voiceIcon, voices);
    }
    
    /**
     * Add error text to the pane.
     */
    private VBox addErrorText(){
    	VBox plusErrorPane = new VBox();
    	
    	error = new Text("");
		error.setFont(Font.font ("Verdana", 16));
		error.setFill(Color.RED);
    	
    	plusErrorPane.setAlignment(Pos.CENTER);
    	plusErrorPane.setMaxWidth(1100);
    	plusErrorPane.setPadding(new Insets(10));
		
		plusErrorPane.getChildren().addAll(error, buttonVoicePane);
		
		return plusErrorPane;
	}

    /**
     * Set up the text area as search has been completed.
     * @param textArea	the text area to add to this screen
     */
    public void searchComplete(TextArea textArea) {
    	textArea.setMinHeight(350);
    	textArea.setMaxHeight(1100);
    	textArea.setWrapText(true);
    	textArea.setStyle("-fx-font: 16px \"Verdana\";");
    	
    	textArea.setOnMouseClicked(e -> {error.setText("");});
    	textArea.setOnMousePressed(e -> {error.setText("");});
    	
    	this.textArea=textArea;
    	Styling.textArea(this.textArea);
		BorderPane.setAlignment(textArea, Pos.TOP_CENTER);
		screen.setCenter(this.textArea);
    }
    
    /**
     * Play/preview button is clicked.
     */
	private void playClicked(){
		if(validSelection()) {
			scripts.getScript("selectSave", new String[]{formatSelected(), "temp",voices.getSelectionModel().getSelectedItem()});
			Media audio = new Media(new File("./audio/temp.wav").toURI().toString());
			playBtn.audioPlayed(audio);
			removeTemp();
		}
	}

	/**
	 * Save button is clicked.
	 */
	private void saveClicked(){
		if(validSelection()) {
			savedChunks++;
			String name = Integer.toString(savedChunks);
			Process process = scripts.getScript("selectSave", new String[]{formatSelected(), name, voices.getSelectionModel().getSelectedItem()});
			if (process.exitValue() == 1){
				error.setText("Can't play selected text: Please change settings or a new chunk");
			}else{
				chunkTable.refreshTable();
			}
		}
	}
	
	/**
	 * Get the user selected text and format it to remove newlines, apostrophes and slashes.
	 * @return	the formatted selected text
	 */
	private String formatSelected() {
		String selected = textArea.getSelectedText();
		selected = selected.trim();
        selected = selected.replaceAll("\n"," ");
		selected = selected.replaceAll("'","");
		selected = selected.replaceAll("\"","");
		return selected;
	}
	
	/**
	 * check if the user selected text is valid.
	 * @return	true	selected text is valid
	 * 			false 	selected text is invalid
	 */
	private boolean validSelection() {
		error.setText("");
        String selected = formatSelected();
        if (selected == null || selected.isEmpty()) {
        	error.setText("Please select a chunk of text");
            return false;
        }

        String[] words = selected.split("\\s+");
        if(words.length > 30){
        	error.setText("Please select a shorter text chunk");
            return false;
        }
		return true;
	}
	
	/**
	 * Remove the temporary files made to preview a chunk.
	 */
	private void removeTemp() {
		String cmd = "rm -f ./audio/temp.wav";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String cmd2 = "rm -f ./audio/temp.txt";
		ProcessBuilder pb2 = new ProcessBuilder("bash", "-c", cmd2);
		try {
			pb2.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
