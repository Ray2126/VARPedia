package varpedia.creating;

import gemmas.main.Line;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
	public TextViewer(VoiceViewer voiceDisp){
		scripts = new Scripts();
		scripts.getScript("cleanup", new String[]{});
		saved = 0;
	    textArea = new TextArea();
	    settings = new BorderPane();
	    bottomOptions = new BorderPane();
	    shell = new BorderPane();
	    this.voiceDisp = voiceDisp;
    }

    public BorderPane getView(){
		setSearched("banana");
		return shell;
	}

    private void createShell(){
		loadOptions();
		shell.setTop(settings);
		setText();
		addPlaySave();
	}

    private void loadOptions(){
		ObservableList<String> voiceOptions =
				FXCollections.observableArrayList(
						"Option 1",
						"Option 2",
						"Option 3"
				);
		final ComboBox voices = new ComboBox(voiceOptions);

		ObservableList<String> synOptions =
				FXCollections.observableArrayList(
						"Option 1",
						"Option 2",
						"Option 3"
				);
		final ComboBox syns = new ComboBox(synOptions);

		HBox options = new HBox();
		options.getChildren().addAll(voices, syns);
		options.setAlignment(Pos.CENTER);
		settings.setRight(options);
	}

    public void setSearched(String searched){
	    searchTerm = searched;
        Text searchTermScreen = new Text(searchTerm);
        HBox text = new HBox();
        text.getChildren().add(searchTermScreen);
        text.setAlignment(Pos.CENTER_LEFT);
        settings.setLeft(text);
		createShell();
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
		HBox playSave = new HBox();
		Button play = new Button("Play Selected");
		play.setOnAction(e -> playClicked());
		Button save = new Button("Save Selected");
		save.setOnAction(e -> saveClicked());
		playSave.setAlignment(Pos.BASELINE_RIGHT);
		playSave.getChildren().addAll(play, save);
		bottomOptions.setRight(playSave);
		shell.setBottom(bottomOptions);
	}

	private void playClicked(){
		String selected = textArea.getSelectedText();
		selected.replaceAll("\n"," ");
		System.out.println(selected);
		Scripts scripts = new Scripts();
		scripts.getScript("selectPlay", new String[]{selected});
	}

	private void saveClicked(){
		saved++;
		String name = Integer.toString(saved);
		String selected = textArea.getSelectedText();
		selected.replaceAll("\n"," ");
		System.out.println(selected);
		scripts.getScript("selectSave", new String[]{selected, name});
		voiceDisp.loadAudio();
	}
}
