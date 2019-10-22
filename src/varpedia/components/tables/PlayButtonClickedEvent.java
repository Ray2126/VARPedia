package varpedia.components.tables;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;

public class PlayButtonClickedEvent extends ActionEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final EventType<PlayButtonClickedEvent> PLAY_CLICKED = new EventType<>(Event.ANY, "PLAY_CLICKED");
	
	public PlayButtonClickedEvent() {
		super();
	}
}
