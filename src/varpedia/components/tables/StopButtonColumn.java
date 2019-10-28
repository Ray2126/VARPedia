package varpedia.components.tables;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import varpedia.components.StopPlayButton;

public class StopButtonColumn<T> extends TableColumn< T, Button> {
	
	private TableView<T> tableView;
	private List<StopPlayButton> stopButtonList = new ArrayList<StopPlayButton>();
	private StopPlayButton stopButton;

	public StopButtonColumn(TableView<T> tv) {
		this.setMinWidth(100);
		this.setStyle("-fx-font: 16px \"Verdana\";");
		
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
				    		tableView.getSelectionModel().select(getTableRow().getIndex());
				    		if(!stopButtonList.contains(stopButton)) {
					    		stopButtonList.add(stopButton);
				    		}
					    	StopButtonClickedEvent.fireEvent(tableView, new StopButtonClickedEvent());
					    	
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
		
		this.tableView = tv;
	}
	
	public StopPlayButton getStopButton() {
		return stopButton;
	}
	
	public void stopMedia() {
		for(StopPlayButton s : stopButtonList) {
			s.getMediaPlayer().stop();
		}
	}	
}
