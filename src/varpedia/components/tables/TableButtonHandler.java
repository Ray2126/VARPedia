package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * The event handler that handles the events fired by the tables
 *
 */
public class TableButtonHandler implements EventHandler<ActionEvent>{

	//These methods are to be overrided by the class that uses this handler
	public void handlePlay(PlayButtonClickedEvent e) {}

	public void handleDelete(DeleteButtonClickedEvent e) {}

	public void handleStop(StopButtonClickedEvent e) {}
	
	@Override
	public void handle(ActionEvent e) {
		if(e instanceof DeleteButtonClickedEvent) {
			handleDelete(new DeleteButtonClickedEvent());
		}
		else if(e instanceof PlayButtonClickedEvent){
			handlePlay(new PlayButtonClickedEvent());
		}
		else if(e instanceof StopButtonClickedEvent) {
			handleStop(new StopButtonClickedEvent());
		}
	}

}
