package varpedia.helper;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class Styling {
	public static void blueButton(Button button) {
		button.setStyle("-fx-background-color: linear-gradient(#5be6ff, #53bff5),linear-gradient(#68d9fc, #6cc4f0), linear-gradient(#6cc4f0, #7ecbf1),linear-gradient(#5bd8e9 0%, #62bdeb 50%, #62c2f1 100%),linear-gradient(from 0% 0% to 15% 50%,rgba(255, 255, 255, 0.9),rgba(255, 255, 255, 0));-fx-background-radius: 25;-fx-background-insets: 0, 1, 2, 1, 0;-fx-text-fill: white;-fx-font-weight: bold;-fx-font-size: 16px;-fx-padding: 5 20 5 20;");
	}
	
	public static void yellowBG(Node node) {
		node.setStyle("-fx-background-color: #F8C11C;");
	}
	
	public static void textColor(Node node) {
		node.setStyle("-fx-font-weight: bold; -fx-text-fill: #4C4C4C");
	}
}
