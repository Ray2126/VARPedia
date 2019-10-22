package varpedia.view;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Callback;
import varpedia.components.tables.Creation;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.components.videoPlayer.VideoPlayer;
import varpedia.create.Scripts;
import varpedia.components.tables.PlayButtonColumn;

/**
 * The table of creations seen on the view creations screen
 *
 */
public class CreationTable extends TableView<Creation>{
	
	Creation _selectedCreation;
	VideoPlayer _videoPlayer;
	
	public CreationTable(VideoPlayer videoPlayer) {
		_videoPlayer = videoPlayer;
		this.setStyle("-fx-font: 16px \"Verdana\";");
		
		//Number column
		TableColumn<Creation, Integer> numberColumn = new TableColumn<>("#");
        numberColumn.setMinWidth(50);
        numberColumn.setStyle("-fx-alignment: CENTER");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        //Name Column
        TableColumn<Creation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(363);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Search term Column
        TableColumn<Creation, String> searchColumn = new TableColumn<>("Topic");
        searchColumn.setMinWidth(363);
        searchColumn.setCellValueFactory(new PropertyValueFactory<>("search"));
        
//    	//Play buttons
//  		TableColumn<Creation, Boolean> playButtonColumn = new TableColumn<Creation, Boolean>("Play:");
//  		playButtonColumn.setMinWidth(100);
//  		playButtonColumn.setStyle("-fx-font: 16px \"Verdana\";");
//  	    playButtonColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Creation, Boolean>, ObservableValue<Boolean>>() {
//  	        @Override 
//  	        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Creation, Boolean> f) {
//  	          return new SimpleBooleanProperty(f.getValue() != null);
//  	        }
//  	    });
//  	    // create a cell value factory with an add button for each row in the table.
//	    playButtonColumn.setCellFactory(new Callback<TableColumn<Creation, Boolean>, TableCell<Creation, Boolean>>() {
//			@Override 
//			public TableCell<Creation, Boolean> call(TableColumn<Creation, Boolean> e) {
//				return new PlayButtonCell();
//			}
//	    });
        
        TableColumn<Creation, Boolean> playButtonColumn = new PlayButtonColumn<Creation> (this);
	    
    	//Play buttons
  		TableColumn<Creation, Boolean> deleteButtonColumn = new TableColumn<Creation, Boolean>("Delete:");
  		deleteButtonColumn.setMinWidth(100);
  		deleteButtonColumn.setStyle("-fx-font: 16px \"Verdana\";");
  	    deleteButtonColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Creation, Boolean>, ObservableValue<Boolean>>() {
  	        @Override 
  	        public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Creation, Boolean> f) {
  	          return new SimpleBooleanProperty(f.getValue() != null);
  	        }
  	    });
  	    // create a cell value factory with an add button for each row in the table.
	    deleteButtonColumn.setCellFactory(new Callback<TableColumn<Creation, Boolean>, TableCell<Creation, Boolean>>() {
			@Override 
			public TableCell<Creation, Boolean> call(TableColumn<Creation, Boolean> e) {
				return new DeleteButtonCell();
			}
	    });
	    
	    //When they have no creations
        this.setPlaceholder(new Label("You currently have no creations"));
        this.getColumns().addAll(numberColumn, nameColumn, searchColumn, playButtonColumn, deleteButtonColumn);
        refreshTable();
        
	}
	
	public void play() {
		_selectedCreation = getSelectionModel().getSelectedItem();
    	_videoPlayer.playVideo(_selectedCreation);
	}
	
	private class PlayButtonCell extends TableCell<Creation, Boolean> {

		private PauseButton _pauseButton = new PauseButton(20,20);
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell() {
		    _pauseButton.setDisable(false);
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_pauseButton);
		
		    _pauseButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	getSelectionModel().select(getTableRow().getIndex());
			    	_selectedCreation = getSelectionModel().getSelectedItem();
			    	_videoPlayer.playVideo(_selectedCreation);
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
	
	private class DeleteButtonCell extends TableCell<Creation, Boolean> {

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
			    	getSelectionModel().select(getTableRow().getIndex());
			    	_selectedCreation = getSelectionModel().getSelectedItem();
			    	deleteButtonClicked();
			    	refreshTable();
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
	    
    private void deleteButtonClicked() {
		
		if(_selectedCreation != null) {
			
			//Create a dialog for user to confirm their delete selection
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			((Button) confirmAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
			confirmAlert.setTitle("Confirmation");
			confirmAlert.setHeaderText(null);
			confirmAlert.setContentText("You are about to delete " + _selectedCreation.getName() + ". Are you sure?");
			Optional<ButtonType> result = confirmAlert.showAndWait();
			
			if(result.get() == ButtonType.OK) {
				File file = new File("creations/" + _selectedCreation.getName());
				String cmd = "rm -fr creations/"+_selectedCreation.getName();
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
				try {
					pb.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
		
		
    /**
     * Refreshes the view of the table
     */
    public void refreshTable(){
        ObservableList<Creation> creations = FXCollections.observableArrayList();
        Scripts scripts =new Scripts();
        scripts.getScript("listCreations", new String[]{});
        
        
        
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream("listing");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			BufferedReader Buff;
			String strLine;
			//Read File Line By Line
			int index=0;
			while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
				Buff = new BufferedReader(new FileReader("./creations/"+strLine+"/quiz/searchTerm.text"));
				String text = Buff.readLine();
				strLine = strLine.replace("./creations/", "");
				strLine = strLine.replace(".mp4", "");

				creations.add(new Creation(strLine, ++index, text));
			}
	
			//Close the input stream
			fstream.close();
		}catch(Exception e) {
			
		}
		this.setItems(creations);
    	
    }
	
}
