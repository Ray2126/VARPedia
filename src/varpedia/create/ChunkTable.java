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
import varpedia.components.videoPlayer.PauseButton;
import varpedia.components.videoPlayer.TimeSlider;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.imageio.ImageIO;

/**
 * The table of chunks saved on the bottom of the chunk screen
 *
 */
public class ChunkTable extends TableView<Audio>{

    private Scripts scripts;
	private TableColumn<Audio, String> nameColumn;
	private StopButtonColumn<Audio> playColumn;
	private TableColumn<Audio, Boolean> deleteColumn;

    public ChunkTable() {
        scripts = new Scripts();
        setUpTable();
    }

    /**
     * Set up the columns and design of table
     */
    private void setUpTable() {
    	//Name Column
        nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(898);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //Play column
        playColumn = new StopButtonColumn<Audio>(this);
        
        //Delete column
      	deleteColumn = new DeleteButtonColumn<Audio>(this);
      	
      	addButtonHandlers();

        this.setPlaceholder(new Label("You currently have no creations"));
        refreshTable();
        this.getColumns().addAll(nameColumn, playColumn, deleteColumn);
        this.setMaxWidth(1100);
        this.setMaxHeight(200);
        this.setStyle("-fx-font: 16px \"Verdana\";");
    }
    
    /**
     * Add event handlers to the play and delete buttons
     */
    private void addButtonHandlers() {
        this.addEventHandler(ActionEvent.ANY, new TableButtonHandler() {
			@Override
			public void handleStop(StopButtonClickedEvent e) {
		      	playButtonClicked();
			}

			@Override
			public void handleDelete(DeleteButtonClickedEvent event) {
				deleteButtonClicked();
			}
        });
    }
    
    /**
     * When play button is clicked, play chunk
     */
    private void playButtonClicked() {
    	ObservableList<Audio> audioSelected = getSelectionModel().getSelectedItems();
    	Media audio = new Media(new File(audioSelected.get(0).getNumber()+".wav").toURI().toString());
    	playColumn.getStopButton().audioPlayed(audio);
    }
    
    /**
     * Delete audio chunk
     */
    private void deleteButtonClicked(){
        ObservableList<Audio> audioSelected = getSelectionModel().getSelectedItems();
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

	/**
	 * Loads the current chunks and displays in table
	 */
    public void refreshTable(){
        ObservableList<Audio> audios = FXCollections.observableArrayList();
        scripts.getScript("listAudio", null);
        try {
            FileInputStream fstream = new FileInputStream("audios");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String fileNumber;
            while ((fileNumber = br.readLine()) != null)   {
                try {
                    FileInputStream fstream2 = new FileInputStream(fileNumber);
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
                    String chunkText;
                    while ((chunkText = br2.readLine()) != null)   {
                        audios.add(new Audio(chunkText, fileNumber));
                    }
                    fstream2.close();
                }catch(Exception e) {}
            }
            fstream.close();
        }catch(Exception e) {}
        
        setItems(audios);
    }
   
    /**
     * Check they have at least one chunk saved
     * @return false  no chunks saved
     * 		   true   at least one chunk saved
     */
    public boolean anySelected() {
        if(this.getItems().isEmpty()){
            Text place = new Text("You must have at least one chunk of audio to continue");
            place.setFont(Font.font(Font.getDefault().getName(),15));
            place.setFill(Color.RED);
            setPlaceholder(place);
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * Returns a pane with the table in the center
     * @return  pane	the pane containing table
     */
    public VBox getMainPane(){
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(this);
        return pane;
    }

}