package varpedia;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The table of creations seen on the main screen
 *
 */
public class CreationTable extends TableView<Creation>{
	
	public CreationTable() {
		//Number column
		TableColumn<Creation, Integer> numberColumn = new TableColumn<>("#");
        numberColumn.setMinWidth(50);
        numberColumn.setStyle("-fx-alignment: CENTER");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        //Name Column
        TableColumn<Creation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(927);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        this.setPlaceholder(new Label("You currently have no creations"));
        loadCreations();
        this.getColumns().addAll(numberColumn, nameColumn);

	}
	
    /**
     * Refreshes the view of the table
     */
    public void loadCreations(){
        //Get all the creations in the creations folder
    	File creationsFolder = new File("creations");
    	File[] creations = creationsFolder.listFiles();
    	
		ObservableList<Creation> creationFileNames = FXCollections.observableArrayList();
		if(creations != null){
            for(int i = 0; i < creations.length; i++) {
                //Remove extension from file name
                String creationNameWithoutExtension = creations[i].getName().substring(0, creations[i].getName().lastIndexOf('.'));
                creationFileNames.add(new Creation(creationNameWithoutExtension, i+1));
            }
            this.setItems(creationFileNames);
        }
    	
    }
	
}
