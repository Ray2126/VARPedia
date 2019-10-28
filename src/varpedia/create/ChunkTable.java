package varpedia.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.components.tables.Audio;
import varpedia.components.tables.DeleteButtonClickedEvent;
import varpedia.components.tables.DeleteButtonColumn;
import varpedia.components.tables.StopButtonClickedEvent;
import varpedia.components.tables.StopButtonColumn;
import varpedia.components.tables.TableButtonHandler;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * The table of chunks saved on the bottom of the chunk screen.
 *
 */
public class ChunkTable{
	
	TableView<Audio> table;
    /**
     * The column for the chunks information - displays text in the chunk.
     */
	private TableColumn<Audio, String> chunkColumn;
	
	/**
	 * The column with the play buttons.
	 */
	private StopButtonColumn<Audio> playColumn;
	
	/**
	 * The column with the delete buttons.
	 */
	private TableColumn<Audio, Boolean> deleteColumn;
	
	/**
	 * Error text when at least one chunk is not created
	 */
	private Text errorText;
	
	private Text placeHolderText;
	
	private Label placeHolderLabel;
	
	/**
	 * Linux scripts.
	 */
    private Scripts scripts;
    

	/**
	 * Constructor.
	 */
    public ChunkTable() {
    	table = new TableView<Audio>();
        scripts = new Scripts();
        chunkColumn = new TableColumn<>("Chunks");
        playColumn = new StopButtonColumn<Audio>(table);
      	deleteColumn = new DeleteButtonColumn<Audio>(table);
      	
        setUpTable();
        
        Styling.tableView(table);
    }

    /**
     * Set up the columns and design of table.
     */
    @SuppressWarnings("unchecked")
	private void setUpTable() {
        chunkColumn.setMinWidth(880);
        chunkColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
      	
      	addButtonHandlers();
      	setUpError();
      	setUpPlaceHolder();
        refreshTable();
        
        table.getColumns().addAll(chunkColumn, playColumn, deleteColumn);
        table.setMaxWidth(1100);
        table.setMaxHeight(230);
        table.setStyle("-fx-font: 16px \"Verdana\";");
    }
    
    /**
     * Add event handlers to the play and delete buttons.
     */
    private void addButtonHandlers() {
    	table.addEventHandler(ActionEvent.ANY, new TableButtonHandler() {
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
     * When play button is clicked, play chunk.
     */
    private void playButtonClicked() {
    	ObservableList<Audio> audioSelected = table.getSelectionModel().getSelectedItems();
    	Media audio = new Media(new File(audioSelected.get(0).getNumber()+".wav").toURI().toString());
    	playColumn.getStopButton().audioPlayed(audio);
    }
    
    /**
     * Delete audio chunk.
     */
    private void deleteButtonClicked(){
        ObservableList<Audio> audioSelected = table.getSelectionModel().getSelectedItems();
        if(audioSelected.size() != 0) {
        	//Get confirmation of delete
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Are you sure?");
        	alert.setHeaderText("Are you sure you want to delete this audio chunk?");
        	
        	DialogPane dialogPane = alert.getDialogPane();
        	Styling.dialogStyle(dialogPane);

        	ButtonType buttonTypeTwo = new ButtonType("Yes");
        	ButtonType buttonTypeOne = new ButtonType("No");
        	
        	alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeTwo){
            	stopMedia();
                scripts.getScript("deleteAudio", new String[]{audioSelected.get(0).getNumber()});
                refreshTable();
            } else {
                return;
            }
        }
    }
    
    /**
     * Set up the error text for when they have no creations and press next
     */
    private void setUpError() {
    	errorText = new Text("You must have at least one chunk saved to continue!");
    	errorText.setFont(Font.font(Font.getDefault().getName(),20));
        errorText.setFill(Color.ORANGERED);
    }
    
    /**
     * Set up the placeholder for when there are no chunks
     */
    private void setUpPlaceHolder() {
      	placeHolderText = new Text("You currently have no chunks");
      	placeHolderLabel = new Label();
      	placeHolderLabel.setText(placeHolderText.getText());
      	
      	placeHolderLabel.setStyle("-fx-text-fill: white;");
      	
      	table.setPlaceholder(placeHolderLabel);
    }
    
    
	/**
	 * Loads the current chunks and displays in table.
	 */
    public void refreshTable(){
        ObservableList<Audio> audios = FXCollections.observableArrayList();
        scripts.getScript("listAudio", null);
        try {
            FileInputStream fstream = new FileInputStream(".temp/audios");
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
        
        table.setItems(audios);
    }
   
    /**
     * Check they have at least one chunk saved.
     * @return false  no chunks saved
     * 		   true   at least one chunk saved
     */
    public boolean anyChunksCreated() {
        if(table.getItems().isEmpty()){
            placeHolderLabel.setText(errorText.getText());
            placeHolderLabel.setTextFill(Color.ORANGERED);
            return false;
        }else{
        	placeHolderLabel.setText(placeHolderText.getText());
        	placeHolderLabel.setTextFill(Color.WHITE);
            return true;
        }
    }
    
    /**
     * Returns a pane with the table in the center.
     * @return  the pane containing table
     */
    public VBox getMainPane(){
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(table);
        return pane;
    }
    
    public void stopMedia() {
    	playColumn.stopMedia();
    }

}
