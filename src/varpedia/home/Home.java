package varpedia.home;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import varpedia.create.CreatorMain;
import varpedia.helper.Styling;
import varpedia.quiz.QuizNavigator;
import varpedia.view.CreationsViewerScreen;

/**
 * The home screen of VARpedia
 * Navigates and manages the current screen
 *
 */
public class Home extends BorderPane{
	
	private Label title;
	private Button play, viewCreations, newCreation, help, exit;
	private Stage primaryStage;
	private Stage creationsStage;
	private Stage quizStage;
	private Stage helpStage;
	
	/**
	 * Loads buttons styling and adds everything to the screen
	 * @param primaryStage
	 */
	public Home(Stage primaryStage) {
		title = new Label("VARpedia");
		title.setFont(new Font("Arial", 60));
		Styling.textColor(title);
		
		play = new Button("Play Learning Game");
		Styling.blueButton(play);		
		setQuiz();
		
		viewCreations = new Button("View My Creations");
		Styling.blueButton(viewCreations);
		setViewCreations();
		
		newCreation = new Button("Create New");
		Styling.blueButton(newCreation);
		setNewCreation();
		
		help = new Button("Get help");
		Styling.blueButton(help);
		setHelp();
		
		exit = new Button("Quit");
		Styling.blueButton(exit);
		setExit();
		
		VBox buttons = new VBox();
		
		buttons.getChildren().addAll(title, play, viewCreations, newCreation, help, exit);
		Styling.yellowBG(buttons);
		this.setLeft(buttons);
		ImageView iv = loadImage();
		BorderPane.setAlignment(iv, Pos.CENTER);
		BorderPane.setMargin(iv, new Insets(10,50,10,10));
		this.setRight(iv);
		
		Styling.yellowBG(this);
		this.primaryStage = primaryStage;
		creationsStage = new Stage();
		quizStage = new Stage();
		
		this.primaryStage.setResizable(false);
		creationsStage.setResizable(false);
		quizStage.setResizable(false);
		
		buttons.setSpacing(20);
		buttons.setPadding(new Insets(20,20,20,20));
		buttons.setAlignment(Pos.CENTER_LEFT);
	}
	
	/**
	 * Loads the main menu image
	 * @return An instance of the node with the image
	 */
	public ImageView loadImage() {
		// load the image
	    File file = new File("./resources/book.png");
	    Image image = new Image(file.toURI().toString());

        // simple displays ImageView the image as is
        ImageView iv = new ImageView();
        iv.setImage(image);

        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        iv.setImage(image);
        iv.setFitWidth(300);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        return iv;
	}
	
	/**
	 * Show the home screen
	 */
	public void showHome() {
		if(creationsStage.isShowing()) {
			creationsStage.close();
		}else if(quizStage.isShowing()) {
			quizStage.close();
		}
		primaryStage.show();
	}
	
	/**
	 * Add functionality to viewCreations button
	 */
	private void setViewCreations() {
		viewCreations.setOnAction(e ->{
			creationsStage.sizeToScene();
			BorderPane layout = new BorderPane();
		    
			CreationsViewerScreen creations = new CreationsViewerScreen(this);
			
			layout.setCenter(creations.getScreen());
			Scene creationsScene = new Scene(layout);
			creationsStage.setScene(creationsScene);
			primaryStage.close();
			creationsStage.setTitle("VARpedia Creations");
			creationsStage.show();
		});
	}
	
	/**
	 * Add functionality to newCreation button
	 */
	private void setNewCreation() {
		newCreation.setOnAction(e ->{
			CreatorMain creator = new CreatorMain(this);
			primaryStage.close();
			
			//Create temporary folders
			File tempFolder = new File(".temp");
			tempFolder.mkdir();
			File audioFolder = new File(".temp/audio");
			audioFolder.mkdir();
			File imagesFolder = new File(".temp/images");
			imagesFolder.mkdir();
			File selectedImagesFolder = new File(".temp/selectedImages");
			selectedImagesFolder.mkdir();
			File resampledAudioFolder = new File(".temp/resampledAudio");
			resampledAudioFolder.mkdir();
			
			creator.beginCreate();
		});
	}
	
	/**
	 * Loads the help manual
	 */
	private void setHelp(){
		help.setOnAction(e -> {
			helpStage = new Stage();
			VBox comp = new VBox();
			Label title = new Label("VARpedia User Manual");
			TextArea help = new TextArea();
			
			comp.setAlignment(Pos.CENTER);
			comp.setMinSize(500, 600);
			help.setMinSize(500, 500);
			comp.setSpacing(15);
			
			title.setFont(Font.font("Verdana", 20));
			
			Styling.textColorNotBold(title);
			Styling.textArea(help);
			Styling.yellowBG(comp);
			
			try {
				FileInputStream fstream = new FileInputStream("./resources/man.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

				String strLine;
				while ((strLine = br.readLine()) != null) {
					//append content of text file to TextArea output
					help.appendText(strLine.trim()+ "\n");
				}
				
				fstream.close();

			}catch(Exception err) {
				err.printStackTrace();
			}
			
			comp.getChildren().addAll(title, help);
	
			Scene stageScene = new Scene(comp, 500, 600);
			helpStage.setScene(stageScene);
			helpStage.setTitle("Help Manual");
			helpStage.show();
		});
	}
	
	/**
	 * Sets the active window as the quiz
	 */
	private void setQuiz() {
		play.setOnAction(e -> {
			
			quizStage.sizeToScene();
			BorderPane layout = new BorderPane();
			ScrollPane scroll = new ScrollPane();
			scroll.setContent(layout);
		    
			QuizNavigator quiz = new QuizNavigator(this);
			if(quiz.getQuiz() == null) {
				//Please create a creation first to play the game
				Alert alert = new Alert(AlertType.ERROR);
		    	alert.setTitle("No Creations");
		    	alert.setHeaderText("You need a creation made to play the game.");
		    	alert.setContentText("Please create a creation first.");
		    	DialogPane dialogPane = alert.getDialogPane();
		    	Styling.dialogStyle(dialogPane);

		    	ButtonType okay = new ButtonType("Okay");

		    	alert.getButtonTypes().setAll(okay);

		    	alert.showAndWait();
				return;
			}
			layout.setCenter(quiz.getQuiz());
			Scene quizScene = new Scene(scroll);
			quizStage.setScene(quizScene);
			quizStage.setTitle("VARpedia Quiz");
			primaryStage.close();
			quizStage.show();
		});
	}
	
	/**
	 * Add functionality to exit button
	 */
	private void setExit() {
		exit.setOnAction(e ->{
			primaryStage.close();
			if(helpStage.isShowing()) {
				helpStage.close();
			}
		});
	}
}
