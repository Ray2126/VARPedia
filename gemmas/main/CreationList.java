package gemmas.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreationList {
	//Class for handling the listing of creations and the delete and play functionality

    private TableView<Creation> creationTable;
    private VBox vBox;
    private HBox hBox;
    Scripts scripts;

    //Returns layout of creations and functions
    public VBox getCreationList(){
        if(vBox == null){
            setUp();
            vBox = new VBox();
            vBox.getChildren().addAll(creationTable, hBox);
        }
        return vBox;
    }

    //Set up of the layout
    private void setUp(){
    	scripts = new Scripts();
        //Number Column
        TableColumn<Creation, Integer> numberColumn = new TableColumn<>("Number");
        numberColumn.setMinWidth(50);
        numberColumn.setStyle("-fx-alignment: CENTER-RIGHT");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        //Name Column
        TableColumn<Creation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(620);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        creationTable = new TableView<>();
        creationTable.setPlaceholder(new Label("You currently have no creations"));
        loadCreation();
        creationTable.getColumns().addAll(numberColumn, nameColumn);

        Button playButton = new Button("play");
        playButton.setOnAction(e -> startTask());
        Button deleteButton = new Button("delete");
        deleteButton.setOnAction(e -> deleteButtonClicked());

        hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-alignment: CENTER");
        hBox.getChildren().addAll(playButton, deleteButton);
    }


    //Loads all current creations and displays in table
    public void loadCreation(){
        
        ObservableList<Creation> creations = FXCollections.observableArrayList();
        scripts.getScript("listCreations", new String[]{});
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream("creations");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	
			String strLine;
			//Read File Line By Line
			int index=0;
			while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
				creations.add(new Creation(strLine, ++index));
			}
	
			//Close the input stream
			fstream.close();
		}catch(Exception e) {
			
		}
		creationTable.setItems(creations);
    }
    
    //Make video play in own thread so can do other things whilst it is playing
    public void startTask() 
    {
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
                playButtonClicked();
            }
        };
 
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }  
    
    //Play selected creation
    private void playButtonClicked(){
        ObservableList<Creation> creationSelected;
        creationSelected = creationTable.getSelectionModel().getSelectedItems();
        if(creationSelected.size() != 0) {
        	scripts.getScript("play", new String[]{creationSelected.get(0).toString()});
        }
    }

    //Delete selected creation
    private void deleteButtonClicked(){
        ObservableList<Creation> creationSelected;
        creationSelected = creationTable.getSelectionModel().getSelectedItems();
        if(creationSelected.size() != 0) {
	    	Alert del = new Alert(AlertType.INFORMATION, "Are you sure you want to delete " + creationSelected.get(0).toString(), ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = del.showAndWait();
            if (result.get() == ButtonType.YES){
    	        scripts.getScript("delete", new String[]{creationSelected.get(0).toString()});
    			Alert success = new Alert(AlertType.CONFIRMATION, "Successfully deleted " + creationSelected.get(0).toString(), ButtonType.OK);
    			success.showAndWait();
    	        loadCreation();	
            }else {
            	return;
            }
        }
    }
}
