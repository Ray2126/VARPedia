package varpedia;

import java.io.File;
import java.io.FileFilter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import varpedia.videoPlayer.VideoPlayer;

public class QuizNavigator {
	private StackPane quizPanel;
	private int creationAmount;
	private VideoPlayer videoPlayer;
	
	public QuizNavigator() {
		creationAmount = getAmount();
		
        HBox videoBox = new HBox();
        videoBox.setPadding(new Insets(40,40,40,40));
        videoBox.setMaxSize(500,1000);
        videoBox.setAlignment(Pos.CENTER);
        videoPlayer = new VideoPlayer();
        videoBox.getChildren().addAll(videoPlayer);
	}
	
	public StackPane getQuiz() {
		return quizPanel;
	}
	
	public int getAmount() {
		File file = new File("./creations");
		File[] files = file.listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File f) {
		        return f.isDirectory();
		    }
		});
		return files.length;
	}
}
