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

	protected void showFeedback(FeedbackScreen feedback) {
		quizScreen.getChildren().clear();
		quizScreen.getChildren().add(feedback.getScreen());
	}

	protected void closeQuiz() {
		home.showHome();
	}
	
	public VBox getQuiz() {
		if(! question.hasCreations()) {
			return null;
		}
		setQuiz();
		return quizScreen;
	}
	
	private void setQuiz() {
		quizScreen = new VBox();
		vBoxStyling();
		quizScreen.getChildren().add(question.startQuiz());
	}
	
	private void vBoxStyling() {
		Styling.yellowBG(quizScreen);
		quizScreen.setAlignment(Pos.CENTER);
		quizScreen.setPrefWidth(800);
	}
	
}
