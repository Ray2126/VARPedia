package varpedia.creating;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class Music {
	
	private String _name;
	private CheckBox _checkBox;
	private Button _button;
	
	public Music(String name) {
		_name = name;
		_checkBox = new CheckBox();
		_button = new Button("Play");
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public CheckBox getCheckBox() {
		return _checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		_checkBox = checkBox;
	}
	
	public Button getButton() {
		return _button;
	}
	
	public void setButton(Button button) {
		_button = button;
	}
}
