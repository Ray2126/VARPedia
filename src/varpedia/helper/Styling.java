package varpedia.helper;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Helper class to apply css to the various components in this application
 * 
 */
public class Styling {
	public static void blueButton(Button button) {
		button.setStyle("-fx-padding: 8 8 8 8;\n" + 
				"  -fx-background-radius: 8;\n" + 
				"  -fx-background-color: #36b5f5,\n" + 
				"    radial-gradient(center 50% 50%, radius 100%, #36b5f5, #2889b9);\n" + 
				"  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 4, 0, 0, 1);\n" + 
				"  -fx-font-weight: bold;\n" + 
				"  -fx-text-fill: white;\n" + 
				"  -fx-font-size: 1.1em;");
		button.setOnMouseEntered(e -> {
			button.setStyle("-fx-padding: 8 8 8 8;\n" + 
					"  -fx-background-radius: 8;\n" + 
					"  -fx-background-color: #36b5f5,\n" + 
					"    radial-gradient(center 50% 50%, radius 100%, #36b5f5, #36b5f5);\n" + 
					"  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 4, 0, 0, 1);\n" + 
					"  -fx-font-weight: bold;\n" + 
					"  -fx-text-fill: white;\n" + 
					"  -fx-font-size: 1.1em;");
		});
		button.setOnMouseExited(e -> {
			button.setStyle("-fx-padding: 8 8 8 8;\n" + 
					"  -fx-background-radius: 8;\n" + 
					"  -fx-background-color: #36b5f5,\n" + 
					"    radial-gradient(center 50% 50%, radius 100%, #36b5f5, #2889b9);\n" + 
					"  -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 4, 0, 0, 1);\n" + 
					"  -fx-font-weight: bold;\n" + 
					"  -fx-text-fill: white;\n" + 
					"  -fx-font-size: 1.1em;");
		});
	}

	public static void textField(TextField tf) {
		tf.setStyle("-fx-font: 16px \"Verdana\";-fx-background-color: #6E6E6E,radial-gradient(center 50% 50%, radius 100%, #6E6E6E, #4C4C4C); -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 4, 0, 0, 1); -fx-font-weight: bold;"); 
	}
	
	public static void yellowBG(Node node) {
		node.setStyle("-fx-background-color: #F8C11C;");
	}
	
	public static void textColor(Node node) {
		node.setStyle("-fx-font-weight: bold; -fx-text-fill: #4C4C4C");
	}
	
	public static void textColorNotBold(Node node) {
		node.setStyle("-fx-text-fill: #4C4C4C");
	}
	
	public static void errorSearch(Node node) {
		node.setStyle("-fx-font: 16px \"Verdana\";-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #d62929, #b02525); -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 4, 0, 0, 1); -fx-font-weight: bold;"); 
	}
	
	@SuppressWarnings("rawtypes")
	public static void tableView(TableView t) {
		t.getStylesheets().add("css/table.css");
	}
	
	@SuppressWarnings("rawtypes")
	public static void comboBox(ComboBox c) {
		c.getStylesheets().add("css/combo.css");
	}
	
	public static void textArea(TextArea t) {
		t.getStylesheets().add("css/textArea.css");
	}
	
	public static void checkBox(CheckBox c) {
		c.getStylesheets().add("css/checkBox.css");
	}
	
	public static void radioButton(RadioButton r) {
		r.getStylesheets().add("css/radioButton.css");
	}
	
	public static void dialogStyle(DialogPane dialogPane) {
		dialogPane.getStyleClass().add("myDialog");
		dialogPane.getStylesheets().add("css/dialog.css");
	}
}
