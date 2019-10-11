package varpedia.creating;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Callback;
import varpedia.videoPlayer.PauseButton;

public class MusicSelectorScreen extends VBox{

	private MediaPlayer _mediaPlayer;
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
		TableColumn<Music, Boolean> buttonColumn = new TableColumn<Music, Boolean>("Preview:");
		buttonColumn.setMinWidth(300);
	    buttonColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Music, Boolean>, ObservableValue<Boolean>>() {
	        @Override public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Music, Boolean> features) {
	          return new SimpleBooleanProperty(features.getValue() != null);
	        }
	    });
	    
	    // create a cell value factory with an add button for each row in the table.
	    buttonColumn.setCellFactory(new Callback<TableColumn<Music, Boolean>, TableCell<Music, Boolean>>() {
	      @Override public TableCell<Music, Boolean> call(TableColumn<Music, Boolean> personBooleanTableColumn) {
	        return new PlayButtonCell(_musicTable);
	      }
	    });
		
		_musicTable = new TableView<>();
		_musicTable.setItems(getMusic());
		_musicTable.getColumns().addAll(selectColumn, musicColumn, buttonColumn);
		
		
		getChildren().addAll(_title, _musicTable);
		setAlignment(Pos.CENTER);
		
		radioButtonSetUp(selectColumn);
		

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
	
	
	private class PlayButtonCell extends TableCell<Music, Boolean> {

		private PauseButton _pauseButton = new PauseButton();
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell(TableView<Music> table) {
		    _pauseButton.setDisable(false);
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_pauseButton);
		
		    _pauseButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	table.getSelectionModel().select(getTableRow().getIndex());
			    	for(int i = 0; i < 1; i++) {
			    		System.out.println(table.getSelectionModel().getSelectedItems().get(i).getName());
			    	}
			    	
			    }
		    });
	    }

	    /** places an add button in the row only if the row is not empty. */
	    @Override 
	    protected void updateItem(Boolean item, boolean empty) {
	    	super.updateItem(item, empty);
	    	if (!empty) {
	    		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	    		setGraphic(paddedButton);
	    	} else {
	    		setGraphic(null);
	    	}
	    }
  }

}
