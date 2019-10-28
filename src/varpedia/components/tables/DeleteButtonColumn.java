package varpedia.components.tables;

import varpedia.helper.Styling;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import varpedia.helper.LoadIcon;

/**
 * A table column used for any of the tables that need a column of delete buttons
 * @param <T>	the item in the table
 */
public class DeleteButtonColumn<T> extends TableColumn< T, Boolean> {
	
	public DeleteButtonColumn(TableView<T> tv) {
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";-fx-font-weight: bold;");
		
		this.setCellFactory(col -> new TableCell<T, Boolean>() {
			
			private Button deleteButton = new Button();
			private StackPane paddedButton = new StackPane();

		    {
		    	Styling.blueButton(deleteButton);
		    	deleteButton.setGraphic(LoadIcon.loadIcon("delete", 20, 20));
			    paddedButton.setPadding(new Insets(3));
			    paddedButton.getChildren().add(deleteButton);
		    
			    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				    @Override 
				    public void handle(ActionEvent actionEvent) {
				    	tv.getSelectionModel().select(getTableRow().getIndex());
				    	DeleteButtonClickedEvent.fireEvent(tv, new DeleteButtonClickedEvent());
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
		    	}
		    	else {
		    		setGraphic(null);
		    	}
		    }
		});
	}
	
}
