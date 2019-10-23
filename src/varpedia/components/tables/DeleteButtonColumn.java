package varpedia.components.tables;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import varpedia.components.videoPlayer.PauseButton;
import varpedia.helper.LoadIcon;

public class DeleteButtonColumn<T> extends TableColumn< T, Boolean> {

private TableView<T> _tableView;
	
	public DeleteButtonColumn(TableView<T> tableView) {
		this.setText("Delete");
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";");
		this.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, Boolean>, ObservableValue<Boolean>>() {
	      @Override 
	      public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<T, Boolean> f) {
	        return new SimpleBooleanProperty(f.getValue() != null);
	      }
		});
		
		this.setCellFactory(new Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>>() {
			@Override 
			public TableCell<T, Boolean> call(TableColumn<T, Boolean> e) {
				return new DeleteButtonCell();
			}
		});
		_tableView = tableView;
	}
	
	private class DeleteButtonCell extends TableCell< T, Boolean> {

		private Button _deleteButton = new Button();
		private StackPane paddedButton = new StackPane();

	    public DeleteButtonCell() {
	    	_deleteButton.setGraphic(LoadIcon.loadIcon("delete", 20, 20));
		    paddedButton.setPadding(new Insets(3));
		    paddedButton.getChildren().add(_deleteButton);
	    
		    _deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override 
			    public void handle(ActionEvent actionEvent) {
			    	_tableView.getSelectionModel().select(getTableRow().getIndex());
			    	DeleteButtonClickedEvent.fireEvent(_tableView, new DeleteButtonClickedEvent());
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
