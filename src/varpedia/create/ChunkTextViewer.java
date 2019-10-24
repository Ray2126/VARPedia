package varpedia.create;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.components.StopPlayButton;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.components.videoPlayer.TimeSlider;
import varpedia.helper.LoadIcon;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

/**
 * Top half of the chunk screen. Contains text area to edit, play and save chunks, and voice options.
 *
 */
public class ChunkTextViewer extends BorderPane{
	
	private ChunkTable voiceDisp;
	
	private TextArea textArea;
	
	private HBox buttonVoicePane;
	private StopPlayButton _playBtn;
	private Button _saveBtn;
	private ComboBox<String> voices;
	private Text error;
	
	private Scripts scripts;
	
	private int savedChunks;

	public ChunkTextViewer(ChunkTable voiceDisp){
		scripts = new Scripts();
	    this.voiceDisp = voiceDisp;
	    this.setMaxWidth(1100);
    }
	
	/**
	 * Set up this screen 
	 * @param searched  the term the user searched
	 */
    public void setUp(String searched){
    	titleSearchTerm(searched);
		setUpOptionsButtons();
    }
    
    /**
     * Set up the title showing what they searched on top of this screen
     * @param searched	the term the user searched
     */
    private void titleSearchTerm(String searched) {
    	searched = searched.toUpperCase();
        Text searchTerm = new Text(searched);
		searchTerm.setFont(Font.font(Font.getDefault().getName(),26));
		Styling.textColor(searchTerm);
		
        HBox titlePane = new HBox();
		titlePane.setPadding(new Insets(10,10,10,30));
		titlePane.getChildren().add(searchTerm);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        this.setTop(titlePane);
    }

    /**
     * Set up pane for the buttons, voices options and error text
     */
    private void setUpOptionsButtons(){
    	buttonVoicePane = new HBox();
    	buttonVoicePane.setSpacing(10);
		buttonVoicePane.setAlignment(Pos.CENTER);
		buttonVoicePane.setPadding(new Insets(10));
    	addButtons();
		loadVoicesBox();
		addVoiceOptions();
		this.setBottom(addErrorText());
	}
    
    /**
     * Add buttons to the pane
     */
    private void addButtons() {
    	_playBtn = new StopPlayButton(30,30);
		_playBtn.setOnAction(e -> playClicked());
		Styling.blueButton(_playBtn);
		
		_saveBtn = new Button();
		_saveBtn.setGraphic(LoadIcon.loadIcon("save", 30, 30));
		_saveBtn.setOnAction(e -> saveClicked());
		Styling.blueButton(_saveBtn);
		
		//Centre the buttons
    	Region region = new Region();
		region.setMaxWidth(450);
		region.setMinWidth(450);
		buttonVoicePane.getChildren().addAll(region, _playBtn, _saveBtn);
    }

    /**
     * Load the combo box used for choosing voice
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
	}
    
    /**
     * Add the combo box and icon to the pane
     */
    private void addVoiceOptions() {
    	Label voiceIcon = new Label();
		voiceIcon.setGraphic(LoadIcon.loadIcon("voice", 30, 30));
		
		//Anchor voices to right
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		buttonVoicePane.getChildren().addAll(region,voiceIcon, voices);
    }
    
    /**
     * Add error text to the pane
     */
    private VBox addErrorText(){
    	VBox plusErrorPane = new VBox();
    	plusErrorPane.setAlignment(Pos.CENTER);
    	plusErrorPane.setMaxWidth(1100);
		error = new Text("");
		error.setFont(Font.font ("Verdana", 16));
		plusErrorPane.setPadding(new Insets(10));
		error.setFill(Color.RED);
		plusErrorPane.getChildren().addAll(error, buttonVoicePane);
		return plusErrorPane;
	}

    /**
     * Set up the text area as search has been completed
     * @param textArea	the text area to add to this screen
     */
    public void searchComplete(TextArea textArea) {
    	textArea.setMinHeight(350);
    	textArea.setMaxHeight(1100);
    	textArea.setOnMouseClicked(e -> {error.setText("");});
    	textArea.setOnMousePressed(e -> {error.setText("");});
    	this.textArea=textArea;
    	this.setCenter(this.textArea);
    	textArea.setWrapText(true);
    	textArea.setStyle("-fx-font: 16px \"Verdana\";");

		BorderPane.setAlignment(textArea, Pos.TOP_CENTER);
    }
    
    /**
     * Play/preview button is clicked
     */
	private void playClicked(){
		if(validSelection()) {
			scripts.getScript("selectSave", new String[]{getSelectionFormatted(), "temp",voices.getSelectionModel().getSelectedItem()});
			Media audio = new Media(new File("./audio/temp.wav").toURI().toString());
			_playBtn.audioPlayed(audio);
			removeTemp();
		}
	}

	/**
	 * Save button is clicked
	 */
	private void saveClicked(){
		if(validSelection()) {
			savedChunks++;
			String name = Integer.toString(savedChunks);
			Process process = scripts.getScript("selectSave", new String[]{getSelectionFormatted(), name, voices.getSelectionModel().getSelectedItem()});
			if (process.exitValue() == 1){
				error.setText("Can't play selected text: Please change settings or a new chunk");
			}else{
				voiceDisp.refreshTable();
			}
		}
	}
	
	/**
	 * Get the user selected text and format it to remove newlines, apostrophes and slashes
	 * @return	the formatted selected text
	 */
	private String getSelectionFormatted() {
		String selected = textArea.getSelectedText();
		selected = selected.trim();
        selected = selected.replaceAll("\n"," ");
		selected = selected.replaceAll("'","");
		selected = selected.replaceAll("\"","");
		return selected;
	}
	
	/**
	 * check if the user selected text is valid
	 * @return	true	selected text is valid
	 * 			false 	selected text is invalid
	 */
	private boolean validSelection() {
		error.setText("");
        String selected = getSelectionFormatted();
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
	 * Remove the temporary files made to preview a chunk
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
