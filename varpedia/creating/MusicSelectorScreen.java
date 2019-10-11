package varpedia.creating;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MusicSelectorScreen extends VBox{

	private Label _title;
	private TableView<Music> _musicTable;
	
	public MusicSelectorScreen() {
		//Set up label
		_title = new Label("Select Music: ");
		_title.setAlignment(Pos.CENTER);
		_title.setFont(Font.font(Font.getDefault().getName(),20));
		
		//Music column
		TableColumn<Music, String> musicColumn = new TableColumn<>("Music");
		musicColumn.setMinWidth(200);
		musicColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Radio buttons
		TableColumn<Music, RadioButton> selectColumn = new TableColumn<Music, RadioButton>("Select:");
		selectColumn.setCellValueFactory(new PropertyValueFactory<>("radioButton"));
		
		//Play buttons
		TableColumn<Music, Button> buttonColumn = new TableColumn<Music, Button>("Preview:");
		buttonColumn.setMinWidth(300);
		buttonColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
		
		_musicTable = new TableView<>();
		_musicTable.setItems(getMusic());
		_musicTable.getColumns().addAll(selectColumn, musicColumn, buttonColumn);
		
		
		getChildren().addAll(_title, _musicTable);
		setAlignment(Pos.CENTER);
		
		radioButtonSetUp(selectColumn);
		
		//Remove first 
		buttonColumn.getCellObservableValue(0).getValue().setOpacity(0);
		buttonColumn.getCellObservableValue(0).getValue().setDisable(true);
	} 
	
	//Get all of the music from music folder
	private ObservableList<Music> getMusic() {
		File file = new File("../music");
		File[] files = file.listFiles();
		
		ObservableList<Music> musicList = FXCollections.observableArrayList();
		//Add selection for no music
		musicList.add(new Music("No music"));
		
		for(int i = 0; i < files.length; i++) {
			String name = files[i].getName().substring(0, files[i].getName().lastIndexOf('.'));
			musicList.add(new Music(name));
		}
		
		return musicList;
	} 
	
	private void radioButtonSetUp(TableColumn<Music, RadioButton> radioButtonColumn) {
		
		//Only allow one selection
		List<RadioButton> buttons = new ArrayList<>();
		for (Music item : _musicTable.getItems()) {
			buttons.add(radioButtonColumn.getCellObservableValue(item).getValue());
		}
		ToggleGroup group = new ToggleGroup();
		for(RadioButton button : buttons) {
			button.setToggleGroup(group);
		}
		
		//Set no music as default
		radioButtonColumn.getCellObservableValue(0).getValue().setSelected(true);
	}
}
