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
import javafx.util.Callback;
import javafx.scene.layout.StackPane;

public class RadioButtonColumn extends TableColumn<Music, Boolean>{
	
	private TableView<Music> _tableView;
	private Music _selectedMusic;
	
	public RadioButtonColumn(TableView<Music> tableView) {
		super("Select");
		//Set default to no music
		_selectedMusic = new Music("No music");
		_tableView = tableView;
		this.setStyle("-fx-font: 16px \"Verdana\";");
		ToggleGroup group = new ToggleGroup();
		
		this.setCellFactory(col -> new TableCell<Music, Boolean>() {
			
			private RadioButton select;
			private StackPane paddedButton = new StackPane();
			{
				select = new RadioButton();
				select.setToggleGroup(group);
				paddedButton.setPadding(new Insets(3));
			    paddedButton.getChildren().add(select);
			    
			    select.setOnAction(new EventHandler<ActionEvent>() {

					@Override 
			    	public void handle(ActionEvent actionEvent) {
			    		_tableView.getSelectionModel().select(getTableRow().getIndex());
			    		_selectedMusic = _tableView.getSelectionModel().getSelectedItem();
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
		return _selectedMusic;
	}
	
}
