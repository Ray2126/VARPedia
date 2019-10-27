package varpedia.create;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import varpedia.components.videoPlayer.VideoPlayer;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * The screen where user can preview their almost complete creation. The user will
 * choose a name for their creation on this screen.
 *
 */
public class FinalPreviewScreen{

	private BorderPane screen;
	/**
	 * The video player the plays the preview
	 */
    private VideoPlayer videoPlayer;
    
    /**
     * The text field where user can enter name of creation.
     */
    private TextField nameInput;
    
    /**
     * The error message that will tell the user of invalid creation names.
     */
    private Text invalidNameText;
    
    /**
     * The term the user searched.
     */
    private String searched;
    
    /**
     * Linux scripts.
     */
    private Scripts scripts;
    

    /**
     * Constructor.
     * @param searched	The term the user searched
     */
    public FinalPreviewScreen(String searched){
    	this.searched = searched;
    	screen = new BorderPane();
        scripts = new Scripts();
        nameInput = new TextField();
        videoPlayer = new VideoPlayer();
        
        Styling.yellowBG(screen);
        
        setUp();
    }
    
    /**
     * Set up this screen
     */
    private void setUp() {
    	setUpVideoPlayer();
    	setUpErrorText();
    	setUpNameInput();
    }

    /**
     * Set up the text that will say invalid creation name
     */
    private void setUpErrorText() {
    	invalidNameText = new Text("");
        invalidNameText.setFill(Color.RED);
        invalidNameText.setFont(Font.font(Font.getDefault().getName(),20));
        screen.setCenter(invalidNameText);
	}

    /**
     * Set up the text field where the user enters the name of the creation
     */
	private void setUpNameInput() {
		HBox namePane = new HBox(10);
		
    	nameInput.setMaxWidth(250);
        Styling.textField(nameInput);
        
        Label nameText = new Label("Name: ");
        nameText.setFont(Font.font(Font.getDefault().getName(),20));
        Styling.textColor(nameText);
        
        BorderPane.setAlignment(namePane, Pos.BOTTOM_CENTER);
        namePane.setAlignment(Pos.CENTER);
        namePane.setPadding(new Insets(0,0,30,0));
        
        namePane.getChildren().addAll(nameText, nameInput);

        screen.setBottom(namePane);
	}

	/**
	 * Set up the video player
	 */
	private void setUpVideoPlayer() {
    	HBox videoBox = new HBox();
    	
        videoBox.setPadding(new Insets(40,40,40,40));
        videoBox.setMaxSize(500,1000);
        videoBox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(videoBox, Pos.TOP_CENTER);
        
        videoBox.getChildren().add(videoPlayer);
       
        screen.setTop(videoBox);
	}
	
	/**
	 * Play the preview video
	 */
    public void playVideo() {
        videoPlayer.playPreview();
    }
    
    /**
     * Stop the preview video as moving to a different screen
     */
	public void stopVideo() {
		videoPlayer.stop();
	}

	/**
	 * Make the preview creation, combine the audio and images
	 * @param mainScreen	the creating stage
	 * @param imgCount		number of images to be put in creation
	 */
	public void makePreview(CreatorMain mainScreen, int imgCount) {
		double framerate = findFramerate(imgCount);
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                scripts.getScript("preview", new String[]{String.valueOf(framerate)});
                return null;
            }
        };
        task.setOnSucceeded(e -> {
        	//TODO maybe error handling
        	mainScreen.previewScreenUp();
        });
        
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
	
	/**
	 * Find the framerate of the audio, needed for adding images
	 * @param imgCount	number of images to be put in creation
	 * @return			framerate of audio
	 */
	private double findFramerate(int imgCount) {
		File file = new File("./voice.wav");
        AudioInputStream audioInputStream = null;
        
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double durationInSeconds = (frames+0.0) / format.getFrameRate();
        double imgLength = durationInSeconds/imgCount;
        return 1/imgLength;
	}

	/**
	 * Check if the name the user entered is valid
	 * @return	true	name is valid
	 * 			false 	name is invalid
	 */
    public boolean isValidName() {
    	boolean valid = true;
    	
        if(nameInput.getText().isEmpty()){
        	invalidNameText.setText("Invalid creation name, please try again");
            return false;
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
            	invalidNameText.setText("Invalid creation name, please try again");
                return false;
            }
        }
        
        return true;
    }

    /**
     * Once name is valid, rename the temp created video to entered name or if name is taken
     * create popup asking if they want to override
     * @param mainScreen
     */
    public void createCreation(CreatorMain mainScreen)
    {
        String name = nameInput.getText();
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                Process nameProcess = scripts.getScript("nameValid", new String[] {name});
                if(nameProcess.exitValue() == 1) {
                    return 1;
                }
                return 0;
            }
        };
        task.setOnSucceeded(e -> {
            switch(task.getValue()) {
                case 0:
                    scripts.getScript("name", new String[] {name, searched});
                    Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successfully created " + name, ButtonType.OK);
                	DialogPane dialogPane = success.getDialogPane();
                	Styling.dialogStyle(dialogPane);
                    success.showAndWait();
                    mainScreen.close();
                    break;
                case 1:
                    deleteOrRename(mainScreen);
                    break;
            }

        });
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
    
    /**
     * Pop up window asking if the user wants to override
     * @param mainScreen	the creating stage
     */
    private void deleteOrRename(CreatorMain mainScreen) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    	DialogPane dialogPane = confirmAlert.getDialogPane();
    	Styling.dialogStyle(dialogPane);
		((Button) confirmAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
		((Button) confirmAlert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
		confirmAlert.setTitle("Confirmation");
		confirmAlert.setHeaderText(null);
		confirmAlert.setContentText("Name already taken. Would you like to override?");
		Optional<ButtonType> result = confirmAlert.showAndWait();
		
		if(result.get() == ButtonType.OK) {
        	scripts.getScript("name", new String[]{nameInput.getText(), searched});
        	mainScreen.close();
		}
    }

	public BorderPane getScreen() {
		return screen;
	}


}