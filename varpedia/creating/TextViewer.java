package varpedia.creating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.CreatorMain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

//area to select options and edit text and play selected text chunks
public class TextViewer {
    BorderPane shell;
	TextArea textArea;
	BorderPane settings;
	BorderPane bottomOptions;
	String searchTerm;
    VoiceViewer voiceDisp;
	int saved;
	Scripts scripts;
	VBox settingsBox;
	ComboBox<String> voices;
	ComboBox<String> syns;
	Text error;

	public TextViewer(VoiceViewer voiceDisp){
		scripts = new Scripts();
		scripts.getScript("cleanup", new String[]{});
		saved = 0;
	    textArea = new TextArea();
	    textArea.setOnMousePressed(e -> {error.setText("");});
	    settings = new BorderPane();
		settingsBox = new VBox();
	    bottomOptions = new BorderPane();
	    shell = new BorderPane();
	    this.voiceDisp = voiceDisp;
    }

    public BorderPane getView(){
		return shell;
	}

    private void createShell(){
		loadOptions();
		BorderPane.setAlignment(settings, Pos.TOP_CENTER);
		shell.setTop(settings);
		addPlaySave();
	}

    private void loadOptions(){
		//kal_diphone
		//akl_nz_jdt_diphone
		//"akl_nz_cw_cg_cg"
		ObservableList<String> voiceOptions =
				FXCollections.observableArrayList(
						"kal",
						"nz"
				);
		this.voices = new ComboBox<String>(voiceOptions);
		Text voiceText = new Text("Voice: ");
		Text synsText = new Text("Synthesiser: ");

		ObservableList<String> synOptions =
				FXCollections.observableArrayList(
						"festival",
						"espeak"
				);
		this.syns = new ComboBox<String>(synOptions);
		
		syns.getSelectionModel().selectFirst();
		voices.getSelectionModel().selectFirst();
		
		//Disable voices if they choose espeak
		syns.setOnAction(e -> {
			if(syns.getSelectionModel().getSelectedItem().equals("espeak")) {
				voices.setDisable(true);
			}
			else {
				voices.setDisable(false);
			}
		});

		HBox options = new HBox();
		HBox synBox = new HBox();
		synBox.getChildren().addAll(synsText, syns);
		synBox.setSpacing(5);
		synBox.setAlignment(Pos.CENTER);
		HBox txtBox = new HBox();
		txtBox.getChildren().addAll(voiceText, voices);
		txtBox.setSpacing(5);
		txtBox.setAlignment(Pos.CENTER);
		options.getChildren().addAll(synBox ,txtBox);
		options.setAlignment(Pos.CENTER);
		options.setPadding(new Insets(10,10,10,10));
		options.setSpacing(20);
		settings.setRight(options);
		settings.setMaxWidth(1400);
	}

    public void setSearched(String searched, CreatorMain mainScreen){
	    searchTerm = searched;
        Text searchTermScreen = new Text(searchTerm);
        HBox text = new HBox();
		searchTermScreen.setFont(Font.font(Font.getDefault().getName(),20));
		text.setAlignment(Pos.CENTER);
		text.setPadding(new Insets(10,10,10,10));
		text.getChildren().add(searchTermScreen);
        text.setAlignment(Pos.CENTER_LEFT);
        settings.setLeft(text);
        startTaskSearch(mainScreen);
		createShell();
    }

	//Task for having wikit search in own thread
	public void startTaskSearch(CreatorMain mainScreen) {
		Task<TextArea> task = new Task<TextArea>() {
			@Override protected TextArea call() throws Exception {
				Process process = scripts.getScript("search", new String[] {searchTerm});
				if(process.exitValue() == 1) {
					return null;
				}
				TextArea dummy = new TextArea();
				try {
					// Open the file
					FileInputStream fstream = new FileInputStream(searchTerm+".text");
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

					String strLine;
					//Read File Line By Line
					int index=0;
					while ((strLine = br.readLine()) != null)   {
						// Print the content on the console
						dummy.appendText(strLine.trim()+ "\n");
					}

					//Close the input stream
					fstream.close();

				}catch(Exception e) {

				}
				return dummy;
			}
		};
		task.setOnSucceeded(e -> {
			TextArea dummy = task.getValue();
			if(dummy == null) {
				mainScreen.invalidSearch();
			}else {
				textArea=dummy;
				textArea.setMinHeight(600);
				textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						error.setText("");
					}
				});
				textArea.setMaxWidth(1400);
				shell.setCenter(textArea);
				mainScreen.createScreenUp();
			}
		});
		// Run the task in a background thread
		Thread backgroundThread = new Thread(task);
		// Terminate the running thread if the application exits
		backgroundThread.setDaemon(true);
		// Start the thread
		backgroundThread.start();
	}

	public void setText(){
		if(searchTerm != null){
			try {
				// Open the file
				FileInputStream fstream = new FileInputStream("dummy.text");
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

				String strLine;
				//Read File Line By Line
				int index=0;
				while ((strLine = br.readLine()) != null)   {
					// Print the content on the console
					textArea.appendText(strLine+ "\n");
				}

				//Close the input stream
				fstream.close();

			}catch(Exception e) {

			}
		}
		shell.setCenter(textArea);
    }

    private void addPlaySave(){
		//Can't play selected text: Please change settings or a new chunk
		error = new Text("");
		error.setFill(Color.RED);
		HBox playSave = new HBox();
		Button play = new Button("Play Selected");
		play.setOnAction(e -> playClicked());
		Button save = new Button("Save Selected");
		save.setOnAction(e -> saveClicked());
		playSave.setAlignment(Pos.BOTTOM_RIGHT);
		playSave.getChildren().addAll(error, play, save);
		BorderPane.setAlignment(playSave, Pos.BOTTOM_RIGHT);
		playSave.setPadding(new Insets(10,10,10,10));
		playSave.setSpacing(10);
		playSave.setMaxWidth(1400);
		shell.setMaxWidth(1400);
		shell.setBottom(playSave);
	}

	private void playClicked(){
		error.setText("");
        String selected = textArea.getSelectedText();
        if (selected == null || selected.isEmpty()) {
            return;
        }

        String[] words = selected.split("\\s+");
        if(words.length > 30){
            return;
        }
		selected = selected.replaceAll("\n"," ");
		selected = selected.replaceAll("'","");
		selected = selected.replaceAll("\"","");
		Process process = scripts.getScript("selectPlay", new String[]{selected, syns.getSelectionModel().getSelectedItem(), voices.getSelectionModel().getSelectedItem()});
		if (process.exitValue() == 1){
			error.setText("Can't play selected text: Please change settings or a new chunk");
		}else{
			voiceDisp.loadAudio();
		}
	}

	private void saveClicked(){
		error.setText("");
	    String selected = textArea.getSelectedText();
        if (selected == null || selected.isEmpty()) {
            return;
        }

        String[] words = selected.split("\\s+");
        if(words.length > 30){
            return;
        }
		saved++;
		String name = Integer.toString(saved);
		selected = selected.replaceAll("\n"," ");
		selected = selected.replaceAll("'","");
		selected = selected.replaceAll("\"","");
		Process process = scripts.getScript("selectSave", new String[]{selected, name, syns.getSelectionModel().getSelectedItem(), voices.getSelectionModel().getSelectedItem()});
		if (process.exitValue() == 1){
			error.setText("Can't play selected text: Please change settings or a new chunk");
		}else{
			voiceDisp.loadAudio();
		}
	}
}
