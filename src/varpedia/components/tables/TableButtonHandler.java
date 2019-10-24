package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class TableButtonHandler implements EventHandler<ActionEvent>{
	
	public abstract void handlePlay(PlayButtonClickedEvent e);
	public abstract void handleDelete(DeleteButtonClickedEvent e);
	public abstract void handleStop(StopButtonClickedEvent e);
	
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
