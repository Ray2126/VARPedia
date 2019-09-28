package varpedia.creating;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import varpedia.Creation;
import varpedia.CreatorMain;
import varpedia.VideoPlayer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

//Name creation and preview video of creation
public class FinalPreview {
    VideoPlayer videoPlayer;
    TextField nameInput;
    VBox name;
    VBox screen;

    public FinalPreview(){
        HBox videoBox = new HBox();
        videoBox.setPadding(new Insets(40,40,40,40));
        videoBox.setMaxSize(500,1000);
        videoBox.setAlignment(Pos.CENTER);
        videoPlayer = new VideoPlayer();
        videoBox.getChildren().addAll(videoPlayer);
        nameInput = new TextField();
        Text prompt = new Text("What would you like to name your creation");
        nameInput.setMaxWidth(250);
        name = new VBox();
        name.setAlignment(Pos.CENTER);
        name.setSpacing(10);
        name.getChildren().addAll(prompt, nameInput);
        screen = new VBox();
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(10);
        screen.getChildren().addAll(videoBox, name);
    }

    public void audioAndImageCombine(CreatorMain mainScreen, int imgCount) {
        File file = new File("output.wav");
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double durationInSeconds = (frames+0.0) / format.getFrameRate();
        double imgLength=durationInSeconds/10;
        double framerate=1/imgLength;
        Scripts scripts = new Scripts();
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                Process process = scripts.getScript("preview", new String[]{String.valueOf(framerate)});
                return process.exitValue();
            }
        };
        task.setOnSucceeded(e -> {
            int result = task.getValue();
            if(result != 0) {
                //error
                System.out.println("error on screen amke");
            }else {
                //Should be images but not done yet
                System.out.println("preview made");
                mainScreen.previewScreenUp();
            }
        });
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void setVideoPlayer(){
        //videoPlayer.playVideo();
    }

    public Node getScreen() {
        return screen;
    }

    public void playVideo(Creation preview) {
        videoPlayer.playVideo(preview);
    }
}
