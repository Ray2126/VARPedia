package varpedia.creating;

import java.io.File;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Duration;
import varpedia.videoPlayer.PauseButton;

public class MusicSelectorScreen extends VBox{

	private MediaPlayer _mediaPlayer;
	private Label _title;
	private TableView<Music> _musicTable;
	private Music _selectedMusic;
	
	public MusicSelectorScreen() {
		//Set up label
		_title = new Label("Select background music for creation: ");
		_title.setAlignment(Pos.CENTER);
		_title.setFont(Font.font(Font.getDefault().getName(),30));
		_title.setPadding(new Insets(0,0,50,0));
		
		_selectedMusic = new Music("No music");
		
		//Music column
		TableColumn<Music, String> musicColumn = new TableColumn<>("Music");
		musicColumn.setMinWidth(400);
		musicColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Radio buttons
		TableColumn<Music, Boolean> selectColumn = new TableColumn<Music, Boolean>("Select:");
		selectColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Music, Boolean>, ObservableValue<Boolean>>() {
	        @Override 
	        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Music, Boolean> f) {
		          return new SimpleBooleanProperty(f.getValue() != null);
	        }
	    });
		ToggleGroup group = new ToggleGroup();
		selectColumn.setCellFactory(new Callback<TableColumn<Music, Boolean>, TableCell<Music, Boolean>>() {
			@Override 
			public TableCell<Music, Boolean> call(TableColumn<Music, Boolean> personBooleanTableColumn) {
				return new RadioButtonCell(group);
			}
	    });
		selectColumn.setMinWidth(50);
	
		
		
		//Play buttons
		TableColumn<Music, Boolean> buttonColumn = new TableColumn<Music, Boolean>("Preview:");
		buttonColumn.setMinWidth(100);
	    buttonColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Music, Boolean>, ObservableValue<Boolean>>() {
	        @Override 
	        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Music, Boolean> f) {
	          return new SimpleBooleanProperty(f.getValue() != null);
	        }
	    });
	    
	    // create a cell value factory with an add button for each row in the table.
	    buttonColumn.setCellFactory(new Callback<TableColumn<Music, Boolean>, TableCell<Music, Boolean>>() {
			@Override 
			public TableCell<Music, Boolean> call(TableColumn<Music, Boolean> e) {
				return new PlayButtonCell();
			}
	    });
		
		_musicTable = new TableView<>();
		_musicTable.setItems(getMusic());
		_musicTable.getColumns().addAll(selectColumn, musicColumn, buttonColumn);
		
		_musicTable.setMaxWidth(567);
		_musicTable.setMaxHeight(300);
		
		getChildren().addAll(_title, _musicTable);
		setAlignment(Pos.CENTER);
	} 
	
	//Get all of the music from music folder
	private ObservableList<Music> getMusic() {
		File file = new File("resources/music");
		File[] files = file.listFiles();
		
		ObservableList<Music> musicList = FXCollections.observableArrayList();
		//Add selection for no music
		
		
		for(int i = 0; i < files.length; i++) {
			String name = files[i].getName().substring(0, files[i].getName().lastIndexOf('.'));
			musicList.add(new Music(name));
		}
		musicList.add(new Music("No music"));
		return musicList;
	} 
	
	private class RadioButtonCell extends TableCell<Music, Boolean> {

		private RadioButton _selection = new RadioButton();
		private StackPane paddedButton = new StackPane();

	    public RadioButtonCell(ToggleGroup group) {
	    	_selection.setToggleGroup(group);
	    	
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_selection);
		
		    _selection.setOnAction(new EventHandler<ActionEvent>() {

				@Override 
		    	public void handle(ActionEvent actionEvent) {
		    		_musicTable.getSelectionModel().select(getTableRow().getIndex());
		    		_selectedMusic = _musicTable.getSelectionModel().getSelectedItem();
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
	    		_selection.setSelected(true);
	    	}
	    	else {
	    		setGraphic(null);
	    	}
	    }
	}
	
	
	private class PlayButtonCell extends TableCell<Music, Boolean> {

		private PauseButton _pauseButton = new PauseButton();
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell() {
		    _pauseButton.setDisable(false);
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_pauseButton);
		
		    _pauseButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	_musicTable.getSelectionModel().select(getTableRow().getIndex());

			    	Media audio = new Media(new File("resources/music/" + _musicTable.getSelectionModel().getSelectedItem().getName()+".wav").toURI().toString());
		        	
		        	//Stop previous audio playing
		        	if(_mediaPlayer != null) {
		        		_mediaPlayer.stop();
		        	}
		        	
		        	_mediaPlayer = new MediaPlayer(audio);
		        	_mediaPlayer.play();
		        	
		    		_pauseButton.videoPlayed(_mediaPlayer);
			    	_mediaPlayer.setOnEndOfMedia(new Runnable() {
						@Override
						public void run() {
							_mediaPlayer.seek(Duration.ZERO);
						}
			    	});
			    }
		    });
	    }

	    /** places an add button in the row only if the row is not empty. */
	    @Override 
	    protected void updateItem(Boolean item, boolean empty) {
	    	super.updateItem(item, empty);

	    	if(getTableRow().getIndex() == _musicTable.getItems().size()-1) {
	    		setGraphic(null);
	    	}	    
	    	else if (!empty) {
	    		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	    		setGraphic(paddedButton);
	    	}
	    		
	    	else {
	    		setGraphic(null);
	    	}
	    }
	}
	
	public Music getSelectedMusic() {
		return _selectedMusic;
	}

}
