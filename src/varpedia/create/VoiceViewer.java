package varpedia.create;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import varpedia.components.tables.Audio;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.components.videoPlayer.TimeSlider;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.imageio.ImageIO;

/**
 * The bottom section of the creating chunks screen. Contains the table of audio as well
 * the play and delete buttons
 *
 */
public class VoiceViewer {

    private TableView<Audio> audioTable;
    private VBox mainPane;
    private HBox bottomPane;
    private Scripts scripts;
	private MediaPlayer _mediaPlayer;
	private TimeSlider _timeSlider;
	private PauseButton _pauseButton;
	public ObservableList<Audio> audios;

    public VoiceViewer() {
        scripts = new Scripts();
        
        setUpTable();
        
        _pauseButton = new PauseButton(30,30);

        _timeSlider = new TimeSlider();
        
        Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
        
        //HBox for the buttons
        bottomPane = new HBox();
        bottomPane.setPadding(new Insets(10,10,10,10));
        bottomPane.setSpacing(10);
        bottomPane.setStyle("-fx-alignment: CENTER");
        bottomPane.setMaxWidth(1000);
        bottomPane.getChildren().addAll(region, _timeSlider, _pauseButton);
        
        //Set up vbox for table and buttons
        mainPane = new VBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().addAll(audioTable, bottomPane);
        
    }

    private void setUpTable() {
    	//Name Column
        TableColumn<Audio, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(938);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //Play column
        TableColumn<Audio, Boolean> playColumn = new TableColumn<Audio, Boolean>("Play:");
		playColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Audio, Boolean>, ObservableValue<Boolean>>() {
	        @Override 
	        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Audio, Boolean> f) {
	        	return new SimpleBooleanProperty(f.getValue() != null);
	        }
	    });
    	playColumn.setCellFactory(new Callback<TableColumn<Audio, Boolean>, TableCell<Audio, Boolean>>() {
			@Override 
			public TableCell<Audio, Boolean> call(TableColumn<Audio, Boolean> p) {
				return new PlayButtonCell();
			}
	    });
		playColumn.setMinWidth(50);
		
		//Delete column
		TableColumn<Audio, Boolean> deleteColumn = new TableColumn<Audio, Boolean>("Delete:");
		deleteColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Audio, Boolean>, ObservableValue<Boolean>>() {
	        @Override 
	        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Audio, Boolean> f) {
		          return new SimpleBooleanProperty(f.getValue() != null);
	        }
	    });
    	deleteColumn.setCellFactory(new Callback<TableColumn<Audio, Boolean>, TableCell<Audio, Boolean>>() {
			@Override 
			public TableCell<Audio, Boolean> call(TableColumn<Audio, Boolean> p) {
				return new DeleteButtonCell();
			}
	    });
		deleteColumn.setMinWidth(50);
		
        //Audio Table
        audioTable = new TableView<Audio>();
        audioTable.setPlaceholder(new Label("You currently have no creations"));
        refreshTable();
        audioTable.getColumns().addAll(nameColumn, playColumn, deleteColumn);
        audioTable.setMaxWidth(1100);
        audioTable.setMaxHeight(200);
        audioTable.setStyle("-fx-font: 16px \"Verdana\";");
    }
    
    private class PlayButtonCell extends TableCell<Audio, Boolean> {

		private PauseButton pauseButton = new PauseButton(20,20);
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell() {
		    pauseButton.setDisable(false);
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(pauseButton);
		
		    pauseButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	audioTable.getSelectionModel().select(getTableRow().getIndex());
			    	ObservableList<Audio> audioSelected = getSelected();
			    	Media audio = new Media(new File(audioSelected.get(0).getNumber()+".wav").toURI().toString());
		        	
		        	//Stop previous audio playing
		        	if(_mediaPlayer != null) {
		        		_mediaPlayer.stop();
		        	}
		        	
		        	_mediaPlayer = new MediaPlayer(audio);
		        	_mediaPlayer.play();
		        	
		        	_timeSlider.videoPlayed(_mediaPlayer);
		    		_pauseButton.videoPlayed(_mediaPlayer);
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
	    	}
	    	else {
	    		setGraphic(null);
	    	}
	    }
	}
    
    private class DeleteButtonCell extends TableCell<Audio, Boolean> {

		private Button _deleteButton = new Button();
		private StackPane paddedButton = new StackPane();

	    public DeleteButtonCell() {
	    	BufferedImage image;
			try {
				image = ImageIO.read(new File("resources/icons/delete.png"));
				ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
				imageView.fitHeightProperty().set(20);
				imageView.fitWidthProperty().set(20);
				_deleteButton.setGraphic(imageView);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_deleteButton);
	    
		    _deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	audioTable.getSelectionModel().select(getTableRow().getIndex());
			    	deleteButtonClicked();
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
	    	}
	    	else {
	    		setGraphic(null);
	    	}
	    }
	    
	    //Delete selected creation
	    private void deleteButtonClicked(){
	        ObservableList<Audio> audioSelected = getSelected();
	        if(audioSelected.size() != 0) {
	        	//Get confirmation of delete
	            Alert del = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to delete this audio chunk?", ButtonType.YES, ButtonType.NO);
	            Optional<ButtonType> result = del.showAndWait();
	            if (result.get() == ButtonType.YES){
	                scripts.getScript("deleteAudio", new String[]{audioSelected.get(0).getNumber()});
	                refreshTable();
	            } else {
	                return;
	            }
	        }
	    }
	}
    
    public ObservableList<Audio> getTableList(){
    	return audios;
    }
   
	//Loads all current chunks and displays in table
    public void refreshTable(){
        audios = FXCollections.observableArrayList();
        
        scripts.getScript("listAudio", null);
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream("audios");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                try {
                    // Open the file
                    FileInputStream fstream2 = new FileInputStream(strLine);
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
                    String strLine2;
                    while ((strLine2 = br2.readLine()) != null)   {
                        // Print the content on the console
                        audios.add(new Audio(strLine2, strLine));
                    }
                    //Close the input stream
                    fstream2.close();
                }catch(Exception e) {
                	
                }
            }

            //Close the input stream
            fstream.close();
        }catch(Exception e) {
        	
        }
        
        audioTable.setItems(audios);
    }
    
    //Get the currently selected items in table
    public ObservableList<Audio> getSelected() {
    	return audioTable.getSelectionModel().getSelectedItems();
    }
   
    //Check if anything in the table is selected
    public boolean anySelected() {
        if(audioTable.getItems().isEmpty()){
            Text place = new Text("You must have at least one chunk of audio saved to continue");
            place.setFont(Font.font(Font.getDefault().getName(),15));
            place.setFill(Color.RED);
            audioTable.setPlaceholder(place);
            return false;
        }else{
            return true;
        }
    }
    
    //Returns the pane containing the table and buttons
    public VBox getMainPane(){
        return mainPane;
    }

}
