package gemmas.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    CreationList creations;
    NewCreation newCreation;
    BorderPane layout;
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("wiki speak");
        layout = new BorderPane();

        creations = new CreationList();
        newCreation = new NewCreation();

      //Navigation for all tabs
        Button newButton = new Button("create new");
        newButton.setOnAction(e -> layout.setCenter(newCreation.getCreationList()));
        Button creationButton = new Button("view creations");
        creationButton.setOnAction(e -> {
        	layout.setCenter(creations.getCreationList());
        	creations.loadCreation();
        	});

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(creationButton, newButton);

        layout.setTop(hBox);
        layout.setCenter(creations.getCreationList());
        scene = new Scene(layout, 700, 400);

        window.setScene(scene);
        window.show();
    }
}
