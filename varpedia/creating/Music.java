package varpedia.creating;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import varpedia.videoPlayer.PauseButton;

public class Music {
	
	private String _name;
	private RadioButton _radioButton;
	
	public Music(String name) {
		_name = name;
		_radioButton = new RadioButton();
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
	
}
