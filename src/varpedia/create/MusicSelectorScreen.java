package varpedia.create;

import java.io.File;

import javafx.collections.FXCollections;
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
public class MusicSelectorScreen{

	/**
	 * The main pane for this screen
	 */
	private VBox screen;
	
	/**
	 * The title - "Select background music"
	 */
	private Label title;
	
	/**
	 * The table of music and buttons on the center of screen.
	 */
	private TableView<Music> musicTable;
	
	/**
	 * The column containing the radio buttons
	 */
	private RadioButtonColumn selectColumn;
	
	/**
	 * The column containing the names of the music
	 */
	private TableColumn<Music, String> musicColumn;
	
	/**
	 * The column containing the preview buttons
	 */
	private TableColumn<Music, Boolean> buttonColumn;
	
	/**
	 * Constructor
	 */
	public MusicSelectorScreen() {
		screen = new VBox();
		title = new Label("Select background music");
		musicTable = new TableView<>();
		musicColumn = new TableColumn<>("Music");
		selectColumn = new RadioButtonColumn(musicTable);
		buttonColumn = new TableColumn<Music, Boolean>();
		
		setUpLabel();
		setUpTable();
		screen.getChildren().addAll(title, musicTable);
		screen.setAlignment(Pos.CENTER);
		Styling.yellowBG(screen);
	}
	
	/**
	 * Set up the label informing them to select background music
	 */
	private void setUpLabel() {
		title.setAlignment(Pos.CENTER);
		title.setFont(Font.font(Font.getDefault().getName(),30));
		Styling.textColor(title);
		title.setPadding(new Insets(0,0,50,0));
	}
	
	/**
	 * Set up the music table
	 */
	@SuppressWarnings("unchecked")
	private void setUpTable() {
		musicTable.setItems(getMusic());
		setUpColumns();
		musicTable.getColumns().addAll(selectColumn, musicColumn, buttonColumn);
		musicTable.setMaxWidth(630);
		musicTable.setMaxHeight(274);
		musicTable.setFixedCellSize(60);
		musicTable.setEditable(false);
		Styling.tableView(musicTable);
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
		musicColumn.setMinWidth(400);
		musicColumn.setStyle("-fx-font: 16px \"Verdana\";");
		musicColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		musicColumn.setSortable(false);
	}

	/**
	 * Set up the radio buttons to allow user to select music
	 */
	private void setUpRadioColumn() {
		selectColumn.setSortable(false);
	}
	
	/**
	 * Set up buttons to allow user to preview music
	 */
	private void setUpPlayColumn() {
		buttonColumn.setMinWidth(100);
		buttonColumn.setStyle("-fx-font: 16px \"Verdana\";");
	    buttonColumn.setCellFactory(col -> new PlayButtonCell() {});
	    buttonColumn.setSortable(false);
	}

	/**
	 * Get the music the user has selected
	 * @return	Music	the music user has selected
	 */
	public Music getSelectedMusic() {
		return selectColumn.getSelectedMusic();
	}

	/**
	 * The individual cells of each play button in the table
	 *
	 */
	private class PlayButtonCell extends TableCell<Music, Boolean> {

		private StopPlayButton stopButton = new StopPlayButton(20,20);
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell() {
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(stopButton);
		
		    stopButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	musicTable.getSelectionModel().select(getTableRow().getIndex());
			    	Media audio = new Media(new File("resources/music/" + musicTable.getSelectionModel().getSelectedItem().getName()+".wav").toURI().toString());
		        	stopButton.audioPlayed(audio);
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
	    	if(getTableRow().getIndex() == musicTable.getItems().size()-1) {
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

	/**
	 * Get the main pane of this screen
	 * @return	the main pane of this screen
	 */
	public VBox getScreen() {
		return screen;
	}
}
