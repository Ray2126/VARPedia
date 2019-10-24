package varpedia.create;

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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

//area to select options and edit text and play selected text chunks
public class TextViewer {
	
    private BorderPane _mainPane;
    private BorderPane _changeVoicesPane;
	private TextArea textArea;
	
	private String searchTerm;
	private VoiceViewer voiceDisp;
	private int saved;
	private Scripts scripts;
	private VBox settingsBox;
	private ComboBox<String> voices;
	private Text error;
	private MediaPlayer _mediaPlayer;
	private StopPlayButton _playBtn;
	private Button _saveBtn;

	public TextViewer(VoiceViewer voiceDisp){
		scripts = new Scripts();
		saved = 0;
	    textArea = new TextArea();
	    textArea.setWrapText(true);
	    textArea.setMaxWidth(1000);
	    _changeVoicesPane = new BorderPane();
		settingsBox = new VBox();
	    _mainPane = new BorderPane();
	    this.voiceDisp = voiceDisp;
    }

    public BorderPane getView(){
		return _mainPane;
	}

    private void createShell(){
		loadOptions();
		BorderPane.setAlignment(_changeVoicesPane, Pos.TOP_CENTER);
		_mainPane.setTop(_changeVoicesPane);
		addPlaySave();
	}

    private void loadOptions(){
		//kal_diphone
		//akl_nz_jdt_diphone
		//"akl_nz_cw_cg_cg"
		ObservableList<String> voiceOptions =
				FXCollections.observableArrayList(
						"American Accent",
						"New Zealand Accent",
						"Robot Voice"
				);
		this.voices = new ComboBox<String>(voiceOptions);
		voices.setStyle("-fx-font: 16px \"Verdana\";");
		
		Label voiceText = new Label();
		voiceText.setGraphic(LoadIcon.loadIcon("voice", 30, 30));
		
		voices.getSelectionModel().selectFirst();
		

		HBox options = new HBox();
		HBox txtBox = new HBox();
		txtBox.getChildren().addAll(voiceText, voices);
		txtBox.setSpacing(5);
		txtBox.setAlignment(Pos.CENTER);
		options.getChildren().addAll(txtBox);
		options.setAlignment(Pos.CENTER);
		options.setPadding(new Insets(10,10,10,10));
		options.setSpacing(20);
		_changeVoicesPane.setRight(options);
		_changeVoicesPane.setMaxWidth(1100);
	}

    public void setSearched(String searched){
	    searchTerm = searched;
        Text searchTermScreen = new Text(searchTerm);
        HBox text = new HBox();
		searchTermScreen.setFont(Font.font(Font.getDefault().getName(),20));
		text.setAlignment(Pos.CENTER);
		text.setPadding(new Insets(10,10,10,10));
		text.getChildren().add(searchTermScreen);
        text.setAlignment(Pos.CENTER_LEFT);
        _changeVoicesPane.setLeft(text);
		createShell();

    }

    public void setTextArea(TextArea textArea) {
    	textArea.setMinHeight(350);
    	textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				error.setText("");
			}
		});
    	textArea.setOnMousePressed(e -> {error.setText("");});
    	this.textArea=textArea;
    	_mainPane.setCenter(this.textArea);
    	textArea.setWrapText(true);
    	textArea.setStyle("-fx-font: 16px \"Verdana\";");
    }
    
    private void addPlaySave(){
		//Can't play selected text: Please change settings or a new chunk
		error = new Text("");
		error.setFont(Font.font ("Verdana", 16));
		error.setFill(Color.RED);
		HBox playSave = new HBox();

		_playBtn = new StopPlayButton(30,30);
		_playBtn.setOnAction(e -> playClicked());
		
		_saveBtn = new Button();
		
		_saveBtn.setGraphic(LoadIcon.loadIcon("save", 30, 30));
		_saveBtn.setOnAction(e -> saveClicked());

		playSave.setAlignment(Pos.BOTTOM_RIGHT);
		
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		playSave.getChildren().addAll(region2, error, _saveBtn, _playBtn, region);
		BorderPane.setAlignment(playSave, Pos.BOTTOM_RIGHT);
		playSave.setPadding(new Insets(10,10,10,10));
		playSave.setSpacing(10);
		playSave.setMaxWidth(1100);
		_mainPane.setMaxWidth(1100);
		_mainPane.setBottom(playSave);
	}

	private void playClicked(){
		error.setText("");
        String selected = textArea.getSelectedText();
        if (selected == null || selected.isEmpty()) {
        	error.setText("Please select a chunk of text");
            return;
        }

        String[] words = selected.split("\\s+");
        if(words.length > 30){
        	error.setText("Please select a shorter text chunk");
            return;
        }
		selected = selected.replaceAll("\n"," ");
		selected = selected.replaceAll("'","");
		selected = selected.replaceAll("\"","");
		
		scripts.getScript("selectSave", new String[]{selected, "temp",voices.getSelectionModel().getSelectedItem()});
		
		Media audio = new Media(new File("./audio/temp.wav").toURI().toString());
    	
		_playBtn.audioPlayed(audio);



		
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

	private void saveClicked(){
		error.setText("");
	    String selected = textArea.getSelectedText();
        if (selected == null || selected.isEmpty()) {
        	error.setText("Please select a chunk of text");
            return;
        }

        String[] words = selected.split("\\s+");
        if(words.length > 30){
        	error.setText("Please select a shorter text chunk");
            return;
        }
		saved++;
		String name = Integer.toString(saved);
		selected = selected.replaceAll("\n"," ");
		selected = selected.replaceAll("'","");
		selected = selected.replaceAll("\"","");
		Process process = scripts.getScript("selectSave", new String[]{selected, name, voices.getSelectionModel().getSelectedItem()});
		if (process.exitValue() == 1){
			error.setText("Can't play selected text: Please change settings or a new chunk");
		}else{
			voiceDisp.refreshTable();
		}
	}
}