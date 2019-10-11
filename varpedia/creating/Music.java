package varpedia.creating;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class Music {
	
	private String _name;
	private RadioButton _radioButton;
	private Button _button;
	
	public Music(String name) {
		_name = name;
		_radioButton = new RadioButton();
		_button = new Button("Play");
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public RadioButton getRadioButton() {
		return _radioButton;
	}

	public void setRadioButton(RadioButton radioButton) {
		_radioButton = radioButton;
	}
	
	public Button getButton() {
		return _button;
	}
	
	public void setButton(Button button) {
		_button = button;
	}
}
