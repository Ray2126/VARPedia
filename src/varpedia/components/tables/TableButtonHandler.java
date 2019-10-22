package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class TableButtonHandler implements EventHandler<ActionEvent>{
	
	public abstract void handlePlay(PlayButtonClickedEvent e);
	public abstract void handleDelete(DeleteButtonClickedEvent event);
	
	@Override
	public void handle(ActionEvent e) {
		if(e instanceof DeleteButtonClickedEvent) {
			handleDelete(new DeleteButtonClickedEvent());
		}
		else if(e instanceof PlayButtonClickedEvent){
			handlePlay(new PlayButtonClickedEvent());
		}
	}

}
