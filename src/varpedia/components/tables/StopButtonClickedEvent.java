package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * The event that is fired when a play/stop button is pressed in any of the tables
 *
 */
public class StopButtonClickedEvent extends ActionEvent{

	private static final long serialVersionUID = 1L;
	
	public static final EventType<StopButtonClickedEvent> STOP_CLICKED = new EventType<>(Event.ANY, "STOP_CLICKED");
	
	public StopButtonClickedEvent() {
		super();
	}
}
