package varpedia.create;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import varpedia.components.StopPlayButton;
import varpedia.components.tables.Music;
import varpedia.components.tables.RadioButtonColumn;
import varpedia.helper.Styling;

/**
 * Screen where the user selects background music
 *
 */
public class MusicSelectorScreen extends VBox{

	/**
	 * The title - "Select background music"
	 */
	private Label _title;
	
	/**
	 * The table of music and buttons on the center of screen.
	 */
	private TableView<Music> _musicTable;
	
	/**
	 * The column containing the radio buttons
	 */
	private RadioButtonColumn _selectColumn;
	
	/**
	 * The column containing the names of the music
	 */
	private TableColumn<Music, String> _musicColumn;
	
	/**
	 * The column containing the preview buttons
	 */
	private TableColumn<Music, Boolean> _buttonColumn;
	
	/**
	 * Constructor
	 */
	public MusicSelectorScreen() {
		setUpLabel();
		setUpTable();
		getChildren().addAll(_title, _musicTable);
		setAlignment(Pos.CENTER);
		Styling.yellowBG(this);
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
		_musicTable.setMaxWidth(630);
		_musicTable.setMaxHeight(274);
		_musicTable.setFixedCellSize(60);
		_musicTable.setEditable(false);
		Styling.tableView(_musicTable);
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
		_musicColumn.setReorderable(false);
	}

	/**
	 * Set up the radio buttons to allow user to select music
	 */
	private void setUpRadioColumn() {
		_selectColumn = new RadioButtonColumn(_musicTable);
		_selectColumn.setSortable(false);
		_selectColumn.setReorderable(false);
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
	    _buttonColumn.setReorderable(false);
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
