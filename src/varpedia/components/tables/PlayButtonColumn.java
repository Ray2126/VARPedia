package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import varpedia.components.videoPlayer.PauseButton;

/**
 * A table column used for any of the tables that need a column of play/pause buttons
 * @param <T>	the item in the table
 */
public class PlayButtonColumn<T> extends TableColumn< T, Boolean> {

	public PlayButtonColumn(TableView<T> tv) {
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";-fx-font-weight: bold;");
		
		this.setCellFactory(col -> new TableCell<T, Boolean>() {
			
			private PauseButton pauseButton = new PauseButton(20,20);
			private StackPane paddedButton = new StackPane();

		    {
			    pauseButton.setDisable(false);
			    paddedButton.setPadding(new Insets(3));
			    paddedButton.getChildren().add(pauseButton);
			
			    pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override 
				    public void handle(ActionEvent actionEvent) {
			    		tv.getSelectionModel().select(getTableRow().getIndex());
				    	PlayButtonClickedEvent.fireEvent(tv, new PlayButtonClickedEvent());
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

