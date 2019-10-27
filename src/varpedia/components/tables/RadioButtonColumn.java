package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import varpedia.helper.Styling;

public class RadioButtonColumn extends TableColumn<Music, Boolean>{
	
	private TableView<Music> tableView;
	private Music selectedMusic;
	
	public RadioButtonColumn(TableView<Music> tv) {
		super();
		
		//Set default to no music
		selectedMusic = new Music("No music");
		tableView = tv;
		
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";");
		ToggleGroup group = new ToggleGroup();
		
		this.setCellFactory(col -> new TableCell<Music, Boolean>() {
			
			private RadioButton select;
			private StackPane paddedButton = new StackPane();
			{
				select = new RadioButton();
				select.setToggleGroup(group);
				Styling.radioButton(select);
				paddedButton.setPadding(new Insets(3));
			    paddedButton.getChildren().add(select);
			    
			    select.setOnAction(new EventHandler<ActionEvent>() {

					@Override 
			    	public void handle(ActionEvent actionEvent) {
			    		tableView.getSelectionModel().select(getTableRow().getIndex());
			    		selectedMusic = tableView.getSelectionModel().getSelectedItem();
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
		    		select.setSelected(true);
		    	}
		    	else {
		    		setGraphic(null);
		    	}
		    }
		});
		
		this.setMinWidth(50);
	}
	
	public Music getSelectedMusic() {
		return selectedMusic;
	}
	
}
