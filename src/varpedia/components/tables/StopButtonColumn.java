package varpedia.components.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import varpedia.components.StopPlayButton;

public class StopButtonColumn<T> extends TableColumn< T, Button> {
	
	private TableView<T> _tableView;
	private List<StopPlayButton> _stopButtonList = new ArrayList<StopPlayButton>();
	private StopPlayButton stopButton;

	public StopButtonColumn(TableView<T> tableView) {
		this.setText("Play");
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";");
//		this.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, Boolean>, ObservableValue<Boolean>>() {
//	      @Override 
//	      public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<T, Boolean> f) {
//	        return new SimpleBooleanProperty(f.getValue() != null);
//	      }
//		});
//		
//		this.setCellFactory(new Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>>() {
//			@Override 
//			public TableCell<T, Boolean> call(TableColumn<T, Boolean> e) {
//				return new StopButtonCell();
//			}
//		});
		
		this.setCellFactory(col -> new TableCell<T, Button>() {
			
			private StackPane paddedButton;
			{
				stopButton = new StopPlayButton(20, 20);
				paddedButton = new StackPane();
				paddedButton.setPadding(new Insets(3));
				
				paddedButton.getChildren().add(stopButton);
				stopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				    @Override 
					    public void handle(MouseEvent actionEvent) {
				    		_tableView.getSelectionModel().select(getTableRow().getIndex());
				    		if(!_stopButtonList.contains(stopButton)) {
					    		_stopButtonList.add(stopButton);
				    		}
					    	StopButtonClickedEvent.fireEvent(_tableView, new StopButtonClickedEvent());
					    	
					    }
				});
			}
			
			@Override 
		    protected void updateItem(Button item, boolean empty) {
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
		
		_tableView = tableView;
	}
	
	public StopPlayButton getStopButton() {
		return stopButton;
	}	
}
