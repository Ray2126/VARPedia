package varpedia.components.tables;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import varpedia.components.videoPlayer.PauseButton;


public class PlayButtonColumn<T> extends TableColumn< T, Boolean> {
	
	private TableView<T> tableView;

	public PlayButtonColumn(TableView<T> tv) {
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";-fx-font-weight: bold;");
		this.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, Boolean>, ObservableValue<Boolean>>() {
	      @Override 
	      public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<T, Boolean> f) {
	        return new SimpleBooleanProperty(f.getValue() != null);
	      }
		});
		
		this.setCellFactory(new Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>>() {
			@Override 
			public TableCell<T, Boolean> call(TableColumn<T, Boolean> e) {
				return new PlayButtonCell();
			}
		});
		tableView = tv;
		

	}
	
	private class PlayButtonCell extends TableCell< T, Boolean> {

		private PauseButton pauseButton = new PauseButton(20,20);
		private StackPane paddedButton = new StackPane();

	    public PlayButtonCell() {
		    pauseButton.setDisable(false);
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(pauseButton);
		
		    pauseButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
			    public void handle(ActionEvent actionEvent) {
		    		tableView.getSelectionModel().select(getTableRow().getIndex());
			    	PlayButtonClickedEvent.fireEvent(tableView, new PlayButtonClickedEvent());
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
	}

	
}

