package varpedia.creating;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class VoiceViewer {
	//Area to see created audio files for each chunk for the current creation being created

    private TableView<Audio> audioTable;
    private VBox vBox;
    private HBox hBox;
    private Scripts scripts;

    //Returns layout of creations and functions
    public VBox getAudioList(){
        if(vBox == null){
            setUp();
            vBox = new VBox();
            audioTable.setMaxHeight(200);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(audioTable, hBox);
        }
        return vBox;
    }

    //Set up of the layout
    private void setUp(){
        scripts = new Scripts();
        //Name Column
        TableColumn<Audio, String> nameColumn = new TableColumn<>("name");
        nameColumn.setMinWidth(1400);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        audioTable = new TableView<Audio>();
        audioTable.setPlaceholder(new Label("You currently have no creations"));
        loadAudio();
        audioTable.getColumns().addAll(nameColumn);
        audioTable.setMaxWidth(1400);

        Button playButton = new Button("play");
        playButton.setOnAction(e -> startTask());
        Button deleteButton = new Button("delete");
        deleteButton.setOnAction(e -> deleteButtonClicked());

        hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-alignment: CENTER");
        hBox.getChildren().addAll(playButton, deleteButton);
    }


    //Loads all current creations and displays in table
    public void loadAudio(){
        ObservableList<Audio> audios = FXCollections.observableArrayList();
        Scripts scripts = new Scripts();
        scripts.getScript("listAudio", null);
        try {
            // Open the file
            FileInputStream fstream = new FileInputStream("audios");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            //Read File Line By Line
            int index=0;
            while ((strLine = br.readLine()) != null)   {
                try {
                    // Open the file
                    FileInputStream fstream2 = new FileInputStream(strLine);
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
                    String strLine2;
                    //Read File Line By Line
                    int index2=0;
                    while ((strLine2 = br2.readLine()) != null)   {
                        // Print the content on the console
                        audios.add(new Audio(strLine2, strLine));
                    }

                    //Close the input stream
                    fstream2.close();
                }catch(Exception e) {

                }
            }

            //Close the input stream
            fstream.close();
        }catch(Exception e) {

        }
        audioTable.setItems(audios);
    }

    //Make video play in own thread so can do other things whilst it is playing
    public void startTask()
    {
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
                playButtonClicked();
            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    //Play selected creation
    private void playButtonClicked(){
        ObservableList<Audio> audioSelected;
        audioSelected = audioTable.getSelectionModel().getSelectedItems();
        if(audioSelected.size() != 0) {
            System.out.println(audioSelected.get(0).getNumber());
            scripts.getScript("playAudio", new String[]{audioSelected.get(0).getNumber()});
        }
    }

    //Delete selected creation
    private void deleteButtonClicked(){
        ObservableList<Audio> audioSelected;
        audioSelected = audioTable.getSelectionModel().getSelectedItems();
        if(audioSelected.size() != 0) {
            Alert del = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to delete " + audioSelected.get(0).toString(), ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = del.showAndWait();
            if (result.get() == ButtonType.YES){
                scripts.getScript("deleteAudio", new String[]{audioSelected.get(0).getNumber()});
                Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successfully deleted " + audioSelected.get(0).toString(), ButtonType.OK);
                success.showAndWait();
                loadAudio();
            }else {
                return;
            }
        }
    }
}
