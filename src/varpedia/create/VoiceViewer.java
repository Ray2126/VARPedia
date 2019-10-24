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
import varpedia.components.StopPlayButton;
import varpedia.components.tables.Audio;
import varpedia.components.tables.DeleteButtonClickedEvent;
import varpedia.components.tables.DeleteButtonColumn;
import varpedia.components.tables.PlayButtonClickedEvent;
import varpedia.components.tables.PlayButtonColumn;
import varpedia.components.tables.StopButtonClickedEvent;
import varpedia.components.tables.StopButtonColumn;
import varpedia.components.tables.TableButtonHandler;
import varpedia.components.tables.TableButtonHandlerAdapter;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.components.videoPlayer.TimeSlider;
import varpedia.helper.Scripts;

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
    private Scripts scripts;
	private MediaPlayer _mediaPlayer;
	public ObservableList<Audio> audios;

    public VoiceViewer() {
        scripts = new Scripts();
        
        setUpTable();

        
        Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
        
        //Set up vbox for table and buttons
        mainPane = new VBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().addAll(audioTable);
        
    }

    private void setUpTable() {
    	audioTable = new TableView<Audio>();
      
    	//Name Column
        TableColumn<Audio, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(898);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //Play column
        StopButtonColumn<Audio> playColumn = new StopButtonColumn<Audio>(audioTable);
        
        //Delete column
      	TableColumn<Audio, Boolean> deleteColumn = new DeleteButtonColumn<Audio>(audioTable);

        audioTable.addEventHandler(ActionEvent.ANY, new TableButtonHandlerAdapter() {

			@Override
			public void handleStop(StopButtonClickedEvent e) {
		      	ObservableList<Audio> audioSelected = getSelected();
		    	Media audio = new Media(new File(audioSelected.get(0).getNumber()+".wav").toURI().toString());
		    	playColumn.getStopButton().audioPlayed(audio);
			}

			@Override
			public void handleDelete(DeleteButtonClickedEvent event) {
				deleteButtonClicked();
			}
        	
        });
        
        //Audio Table
        audioTable.setPlaceholder(new Label("You currently have no creations"));
        refreshTable();
        audioTable.getColumns().addAll(nameColumn, playColumn, deleteColumn);
        audioTable.setMaxWidth(1100);
        audioTable.setMaxHeight(200);
        audioTable.setStyle("-fx-font: 16px \"Verdana\";");
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
