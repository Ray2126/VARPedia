package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * The event that is fired when a delete button is pressed in any of the tables
 *
 */
public class DeleteButtonClickedEvent extends ActionEvent{

	private static final long serialVersionUID = 1L;
	
	public static final EventType<DeleteButtonClickedEvent> DELETE_CLICKED = new EventType<>(Event.ANY, "DELETE_CLICKED");
	
	public DeleteButtonClickedEvent() {
		super();
	}
	
	
	
}
