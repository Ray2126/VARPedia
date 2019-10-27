package varpedia.view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import varpedia.components.tables.DeleteButtonClickedEvent;
import varpedia.components.tables.DeleteButtonColumn;
import varpedia.components.tables.PlayButtonClickedEvent;
import varpedia.components.videoPlayer.VideoPlayer;
import varpedia.helper.Creation;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;
import varpedia.components.tables.PlayButtonColumn;
import varpedia.components.tables.TableButtonHandler;

/**
 * The table of creations seen on the view creations screen
 *
 */
public class CreationTable{
	
	Creation selectedCreation;
	VideoPlayer videoPlayer;
	TableView<Creation> table;
	
	public CreationTable(VideoPlayer vp) {
		videoPlayer = vp;
		table = new TableView<Creation>();

		styleTable();
		initColumns();
		initButtons();
        
        refreshTable();
	}
	
	private void styleTable() {
		table.setStyle("-fx-font: 16px \"Verdana\";");
		table.setMaxWidth(455);
		table.setPrefHeight(453);
		Styling.tableView(table);
		table.setEditable(false);
        table.setPlaceholder(new Label("You currently have no creations"));
	}
	
	/**
	 * Adds event handlers for when a button is pressed in the table
	 */
	private void initButtons() {
        table.addEventHandler(ActionEvent.ANY, new TableButtonHandler() {
          @Override
          public void handlePlay(PlayButtonClickedEvent e) {
            selectedCreation = table.getSelectionModel().getSelectedItem();
              videoPlayer.playVideo(selectedCreation);

          }
          @Override
          public void handleDelete(DeleteButtonClickedEvent event) {
            selectedCreation = table.getSelectionModel().getSelectedItem();
            deleteButtonClicked();
            refreshTable();
          }
        	
        });
	}
	
	@SuppressWarnings("unchecked")
	private void initColumns() {
		/* initialize and specify table column */
        TableColumn<Creation, ImageView> firstColumn = new TableColumn<Creation, ImageView>("");
        firstColumn.setCellValueFactory(new PropertyValueFactory<Creation, ImageView>("image"));
        firstColumn.setPrefWidth(60);  

        //Name Column
        TableColumn<Creation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setPrefWidth(100);
        nameColumn.setStyle("-fx-alignment: CENTER");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Search term Column
        TableColumn<Creation, String> searchColumn = new TableColumn<>("Topic");
        searchColumn.setPrefWidth(100);
        searchColumn.setStyle("-fx-alignment: CENTER");
        searchColumn.setCellValueFactory(new PropertyValueFactory<>("search"));
        
        //Play buttons column
        TableColumn<Creation, Boolean> playButtonColumn = new PlayButtonColumn<Creation> (table);
        
        //Delete Buttons Column
        TableColumn<Creation, Boolean> deleteButtonColumn = new DeleteButtonColumn<Creation> (table);
        
        int buttonWidth = 40;
        
        playButtonColumn.setPrefWidth(buttonWidth);
        deleteButtonColumn.setPrefWidth(buttonWidth);
        playButtonColumn.setMinWidth(buttonWidth);
        deleteButtonColumn.setMinWidth(buttonWidth);
        playButtonColumn.setMaxWidth(buttonWidth);
        deleteButtonColumn.setMaxWidth(buttonWidth);
        
      	Label noCreations = new Label("You currently have no chunks");
      	noCreations.setStyle("-fx-text-fill: white;");
      	table.setPlaceholder(noCreations);
        
        table.getColumns().addAll(firstColumn, nameColumn, searchColumn, playButtonColumn, deleteButtonColumn);
	}
	
	public TableView<Creation> getTable(){
		return table;
	}
	
    private void deleteButtonClicked() {
		if(selectedCreation != null) {
			
			//Create a dialog for user to confirm their delete selection
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			((Button) confirmAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
			confirmAlert.setTitle("Confirmation");
			confirmAlert.setHeaderText("You are about to delete " + selectedCreation.getName());
			confirmAlert.setContentText("Are you sure?");
	    	DialogPane dialogPane = confirmAlert.getDialogPane();
	    	Styling.dialogStyle(dialogPane);
			Optional<ButtonType> result = confirmAlert.showAndWait();
			
			if(result.get() == ButtonType.OK) {
				Scripts scripts = new Scripts();
				scripts.getScript("delCreation", new String[] {selectedCreation.getName()});
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
		table.setItems(creations);
    }
}
