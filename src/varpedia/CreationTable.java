package varpedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import varpedia.creating.Scripts;

/**
 * The table of creations seen on the view creations screen
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
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Search term Column
        TableColumn<Creation, String> searchColumn = new TableColumn<>("Topic");
        searchColumn.setMinWidth(100);
        searchColumn.setCellValueFactory(new PropertyValueFactory<>("search"));
        
        //When they have no creations
        this.setPlaceholder(new Label("You currently have no creations"));
        this.getColumns().addAll(numberColumn, nameColumn, searchColumn);
        refreshTable();
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
