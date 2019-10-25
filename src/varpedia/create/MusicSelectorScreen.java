package varpedia.create;

import java.io.File;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import varpedia.components.StopPlayButton;
import varpedia.components.tables.Music;
import varpedia.components.tables.RadioButtonColumn;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.helper.Styling;

public class MusicSelectorScreen extends VBox{

	private Label _title;
	private TableView<Music> _musicTable;
	private RadioButtonColumn _selectColumn;
	private TableColumn<Music, String> _musicColumn;
	private TableColumn<Music, Boolean> _buttonColumn;
	
	public MusicSelectorScreen() {
		setUpLabel();
		setUpTable();
		getChildren().addAll(_title, _musicTable);
		setAlignment(Pos.CENTER);
	}
	
	/**
	 * Set up the label informing them to select background music
	 */
	private void setUpLabel() {
		_title = new Label("Select background music");
		_title.setAlignment(Pos.CENTER);
		_title.setFont(Font.font(Font.getDefault().getName(),30));
		Styling.textColor(_title);
		_title.setPadding(new Insets(0,0,50,0));
	}
	
	/**
	 * Set up the music table
	 */
	private void setUpTable() {
		_musicTable = new TableView<>();
		_musicTable.setItems(getMusic());
		setUpColumns();
		_musicTable.getColumns().addAll(_selectColumn, _musicColumn, _buttonColumn);
		_musicTable.setMaxWidth(563);
		_musicTable.setMaxHeight(274);
		_musicTable.setFixedCellSize(60);
		_musicTable.setEditable(false);
		
		//Disable drag and drop reordering of columns
		_musicTable.getColumns().addListener(new ListChangeListener() {
	        public boolean suspended;

	        @Override
	        public void onChanged(Change change) {
	            change.next();
	            if (change.wasReplaced() && !suspended) {
	                this.suspended = true;
	                _musicTable.getColumns().setAll(_selectColumn, _musicColumn, _buttonColumn);
	                this.suspended = false;
	            }
	        }
	    });
	}
	
	/**
	 * Get the music files to display to user
	 * @return	ObservableList<Music>	list of music files to add to table
	 */
	private ObservableList<Music> getMusic() {
		File file = new File("resources/music");
		File[] files = file.listFiles();
		ObservableList<Music> musicList = FXCollections.observableArrayList();		
		for(int i = 0; i < files.length; i++) {
			//Remove extension
			String name = files[i].getName().substring(0, files[i].getName().lastIndexOf('.'));
			musicList.add(new Music(name));
		}
		//Add an option for no music
		musicList.add(new Music("No music"));
		return musicList;
	} 
	
	/**
	 * Set up all the columns for the table
	 */
	private void setUpColumns() {
		setUpMusicColumn();
		setUpRadioColumn();
		setUpPlayColumn();
	}
	
	/**
	 * Set up the column showing names of the music
	 */
	private void setUpMusicColumn() {
		_musicColumn = new TableColumn<>("Music");
		_musicColumn.setMinWidth(400);
		_musicColumn.setStyle("-fx-font: 16px \"Verdana\";");
		_musicColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		_musicColumn.setSortable(false);
	}

	/**
	 * Set up the radio buttons to allow user to select music
	 */
	private void setUpRadioColumn() {
		_selectColumn = new RadioButtonColumn(_musicTable);
		_selectColumn.setSortable(false);
	}
	
	/**
	 * Set up buttons to allow user to preview music
	 */
	private void setUpPlayColumn() {
		_buttonColumn = new TableColumn<Music, Boolean>("Preview");
		_buttonColumn.setMinWidth(100);
		_buttonColumn.setStyle("-fx-font: 16px \"Verdana\";");
	    _buttonColumn.setCellFactory(col -> new PlayButtonCell() {});
	    _buttonColumn.setSortable(false);
	}

	/**
	 * Get the music the user has selected
	 * @return	Music	the music user has selected
	 */
	public Music getSelectedMusic() {
		return _selectColumn.getSelectedMusic();
	}

	/**
	 * The individual cells of each play button in the table
	 *
	 */
	private class PlayButtonCell extends TableCell<Music, Boolean> {

		private StopPlayButton _stopButton = new StopPlayButton(20,20);
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell() {
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_stopButton);
		
		    _stopButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	_musicTable.getSelectionModel().select(getTableRow().getIndex());
			    	Media audio = new Media(new File("resources/music/" + _musicTable.getSelectionModel().getSelectedItem().getName()+".wav").toURI().toString());
		        	_stopButton.audioPlayed(audio);
			    }
		    });
	    }

	    /**
	     * Checks if row is not empty, then set the cell to play button
	     */
	    @Override 
	    protected void updateItem(Boolean item, boolean empty) {
	    	super.updateItem(item, empty);

	    	//Remove play button from no music row
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
}
