package varpedia.view;


import java.awt.event.ActionListener;
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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import varpedia.components.tables.DeleteButtonClickedEvent;
import varpedia.components.tables.DeleteButtonColumn;
import varpedia.components.tables.PlayButtonClickedEvent;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.components.videoPlayer.VideoPlayer;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;
import varpedia.components.tables.PlayButtonColumn;
import varpedia.components.tables.TableButtonHandler;
import varpedia.components.tables.TableButtonHandler;

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
		Styling.tableView(this);
		
		//Number column
		TableColumn<Creation, Integer> numberColumn = new TableColumn<>();
        numberColumn.setMinWidth(50);
        numberColumn.setMaxWidth(50);
        numberColumn.setStyle("-fx-alignment: CENTER");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        //numberColumn.setReorderable(false);
        

        //Name Column
        TableColumn<Creation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setStyle("-fx-alignment: CENTER");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //nameColumn.setReorderable(false);

        //Search term Column
        TableColumn<Creation, String> searchColumn = new TableColumn<>("Topic");
        searchColumn.setMinWidth(100);
        searchColumn.setStyle("-fx-alignment: CENTER");
        searchColumn.setCellValueFactory(new PropertyValueFactory<>("search"));
        //searchColumn.setReorderable(false);
        
        //Play buttons column
        TableColumn<Creation, Boolean> playButtonColumn = new PlayButtonColumn<Creation> (this);
        //playButtonColumn.setReorderable(false);
        
        //Delete Buttons Column
        TableColumn<Creation, Boolean> deleteButtonColumn = new DeleteButtonColumn<Creation> (this);
        //deleteButtonColumn.setReorderable(false);

        //Add event handler for when they press a button in table
        this.addEventHandler(ActionEvent.ANY, new TableButtonHandler() {
          @Override
          public void handlePlay(PlayButtonClickedEvent e) {
            _selectedCreation = getSelectionModel().getSelectedItem();
              _videoPlayer.playVideo(_selectedCreation);

          }
          @Override
          public void handleDelete(DeleteButtonClickedEvent event) {
            _selectedCreation = getSelectionModel().getSelectedItem();
            deleteButtonClicked();
            refreshTable();
          }
        	
        });
        
        setEditable(false);
	    //When they have no creations
        this.setPlaceholder(new Label("You currently have no creations"));
        this.getColumns().addAll(numberColumn, nameColumn, searchColumn, playButtonColumn, deleteButtonColumn);
        refreshTable();
        
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
				Scripts scripts = new Scripts();
				scripts.getScript("delCreation", new String[] {_selectedCreation.getName()});
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
