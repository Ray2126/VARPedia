package varpedia.creating;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import varpedia.Creation;
import varpedia.CreatorMain;
import varpedia.videoPlayer.VideoPlayer;

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
    Scripts scripts;
    Text bad;
    String searched;

    public FinalPreview(String searched){
    	this.searched = searched;
        scripts = new Scripts();
        HBox videoBox = new HBox();
        videoBox.setPadding(new Insets(40,40,40,40));
        videoBox.setMaxSize(500,1000);
        videoBox.setAlignment(Pos.CENTER);
        videoPlayer = new VideoPlayer();
        videoBox.getChildren().addAll(videoPlayer);
        nameInput = new TextField();
        Text prompt = new Text("What would you like to name your creation");
        bad = new Text("");
        bad.setFill(Color.RED);
        bad.setFont(Font.font(Font.getDefault().getName(),0));
        nameInput.setMaxWidth(250);
        name = new VBox();
        name.setAlignment(Pos.CENTER);
        name.setSpacing(10);
        name.getChildren().addAll(prompt, bad, nameInput);
        screen = new VBox();
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(10);
        screen.getChildren().addAll(videoBox, name);
    }

    public void audioAndImageCombine(CreatorMain mainScreen, int imgCount) {
        File file = new File("output.wav");
        bad.setFont(Font.font(Font.getDefault().getName(),0));
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
        double imgLength=durationInSeconds/imgCount;
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
            }else {
                //Should be images but not done yet
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

    public Node getScreen() {
        return screen;
    }

    public void playVideo(Creation preview) {
        videoPlayer.playVideo(preview);
    }

    // Deleting creation when have naming conflict
    private void deleteButtonClicked(String name){
        scripts.getScript("name", new String[]{name, searched});
    }

    //Popup window for user to choose whether to delete or rename an existing file
    private void deleteOrRename(String name, CreatorMain mainScreen) {
        Stage window = new Stage();
        bad.setFont(Font.font(Font.getDefault().getName(),0));
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Name Taken");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Creation name "+ name+ " is already taken");
        Button reButton = new Button("Rename");
        Button overButton = new Button("Override");

        overButton.setOnAction(e -> {
            deleteButtonClicked(name);
            window.close();
            mainScreen.close();
        });

        reButton.setOnAction(e -> {
            window.close();
        });
        VBox layout = new VBox(10);
        HBox buttons = new HBox(10);
        buttons.setPadding(new Insets(10,10,10,10));
        layout.setPadding(new Insets(10,10,10,10));
        buttons.getChildren().addAll(reButton, overButton);
        layout.getChildren().addAll(label, buttons);
        buttons.setAlignment(Pos.CENTER);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    public void createNew(CreatorMain mainScreen){
    	bad.setFont(Font.font(Font.getDefault().getName(),0));
        boolean valid = true;
        if(nameInput.getText().isEmpty()){
            bad.setFont(Font.getDefault());
        	bad.setText("Invalid creation name, please try again");
            return;
        }
        char[] a = nameInput.getText().toCharArray();

        for (char c: a)
        {
            valid = ((c >= 'a') && (c <= 'z')) ||
                    ((c >= 'A') && (c <= 'Z')) ||
                    ((c >= '0') && (c <= '9')) ||
                    (c == '_') ||
                    (c == '-');

            if (!valid)
            {
                bad.setFont(Font.getDefault());
            	bad.setText("Invalid creation name, please try again");
                return;
            }
        }
        startTaskCreate( mainScreen);
    }

    //Task to have creating a video in a new thread
    public void startTaskCreate(CreatorMain mainScreen)
    {
        String name = nameInput.getText();
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                Process nameProcess = scripts.getScript("nameValid", new String[] {name});
                if(nameProcess.exitValue() == 1) {
                    return 1;
                }
                //rename preview to creation na,e
                return 0;
            }
        };
        task.setOnSucceeded(e -> {
            switch(task.getValue()) {
                case 0:
                    scripts.getScript("name", new String[] {name, searched});
                    Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successfully created " + name, ButtonType.OK);
                    success.showAndWait();
                    mainScreen.close();
                    //close
                    break;
                case 1:
                    deleteOrRename(name, mainScreen);
                    break;
            }

        });
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

	public void stop() {
		// TODO Auto-generated method stub
		videoPlayer.stop();
	}
}
