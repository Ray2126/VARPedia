package varpedia.quiz;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import varpedia.helper.Styling;
import varpedia.home.Home;

/**
 * This is the main screen for the active learning component
 * It presents the video and direct to results when complete
 *
 */
public class QuizNavigator {
	
	private QuizQuestionScreen question;
	private VBox quizScreen;
	private Home home;
	
	public QuizNavigator(Home home) {
		question = new QuizQuestionScreen(this);
		this.home = home;
	}

	/**
	 * Displays the feedback screen on the quiz window
	 * @param feedback passes the feedback screen with results on it
	 */
	protected void showFeedback(FeedbackScreen feedback) {
		quizScreen.getChildren().clear();
		quizScreen.getChildren().add(feedback.getScreen());
	}

	/**
	 * Closes the quiz window and returns home
	 */
	protected void closeQuiz() {
		home.showHome();
	}
	
	/**
	 * Returns the quiz component if creations exist if not returns null
	 * @return quiz component
	 */
	public VBox getQuiz() {
		if(! question.hasCreations()) {
			return null;
		}
		setQuiz();
		return quizScreen;
	}
	
	/**
	 * Starts the quiz and sets the main screen as the start of the quiz
	 */
	private void setQuiz() {
		quizScreen = new VBox();
		vBoxStyling();
		quizScreen.getChildren().add(question.startQuiz());
	}
	
	/**
	 * Applys the necessary styling to the quiz component
	 */
	private void vBoxStyling() {
		Styling.yellowBG(quizScreen);
		quizScreen.setAlignment(Pos.CENTER);
		quizScreen.setPrefWidth(800);
	}
	
}
