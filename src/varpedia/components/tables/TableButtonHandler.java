package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TableButtonHandler implements EventHandler<ActionEvent>{

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
