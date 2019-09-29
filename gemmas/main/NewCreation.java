package gemmas.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewCreation {
    //Handles the new creation tab
    BorderPane creationTab;
    HBox search;
    HBox options;
    TableView<Line> lineTable;
    Scripts scripts;
    String searchTerm;
    boolean rename;
    boolean hasTerm;
    TextField nameInput;
    TextField searchInput;
    TextField lineInput;
    ObservableList<Line> lineList;


    //Returns the new creation tab
    public BorderPane getCreationList(){
        if(creationTab == null){
            setUp();
            creationTab = new BorderPane();
            creationTab.setTop(search);
            creationTab.setCenter(lineTable);
            creationTab.setBottom(options);
        }
        return creationTab;
    }

    //Handles set up of each area
    public void setUp(){
        scripts = new Scripts();
        searchSetup();
        lineViewSetup();
        optionsSetup();
    }

    //set up of search input and button
    public void searchSetup(){
        search = new HBox();
        Text searchPrompt = new Text("Term to Search: ");
        searchInput = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> loadLines());
        search.setPadding(new Insets(10,10,10,10));
        search.setSpacing(10);
        search.setAlignment(Pos.CENTER);
        search.getChildren().addAll(searchPrompt, searchInput, searchButton);
    }

    //Setup for area where we search
    public void lineViewSetup(){
        //Number Column
        TableColumn<Line, Integer> numberColumn = new TableColumn<>("#");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setStyle("-fx-alignment: CENTER-RIGHT");
        numberColumn.setMinWidth(50);

        //Name Column
        TableColumn<Line, String> textColumn = new TableColumn<>("Text");
        textColumn.setMinWidth(620);
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        lineTable = new TableView<>();
        lineTable.setPlaceholder(new Label("Please search for a term above"));
        lineTable.getColumns().addAll(numberColumn, textColumn);
    }

    //Allows the text column to automatically adjust so width fits the content
    public static void autoResizeColumns( TableView<Line> table )
    {
        //Set the right policy
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            //Minimal width = columnheader
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                //cell must not be empty
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() );
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth( max + 10.0d );
        } );
    }

    //Set up the line, name inputs and create button
    public void optionsSetup(){
        options = new HBox();

        Text linePrompt = new Text("How many lines: ");
        lineInput = new TextField();
//        lineInput.setOnMouseClicked(e -> {
//            lineInput.setStyle("-fx-background-color: #FFF; ");
//        });

        Text namePrompt = new Text("Name of Creation: ");
        nameInput = new TextField();

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> createNew());

        //createButton.setOnAction(e -> create());

        options.setPadding(new Insets(10,10,10,10));
        options.setSpacing(10);
        options.setAlignment(Pos.CENTER);

        options.getChildren().addAll(linePrompt, lineInput, namePrompt, nameInput, createButton);
    }

    //Start creation and send of to the task method
    private void createNew(){

        boolean valid = true;

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
                Alert alert = new Alert(AlertType.WARNING, "Invalid characters in creation name. Please try again . . .", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }
        if(hasTerm) {
            startTaskCreate();
        }
    }

    // Deleting creation when have naming conflict
    private void deleteButtonClicked(String name){
        scripts.getScript("delete", new String[]{name});
    }

    //Popup window for user to choose whether to delete or rename an existing file
    private void deleteOrRename(String name) {
        Stage window = new Stage();

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
            createNew();
        });

        reButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, reButton, overButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Loads the lines for a searched items
    private void loadLines(){
        lineTable.getItems().clear();
        searchTerm = searchInput.getText();
        hasTerm = false;
        if(searchTerm.length() > 0) {
            startTaskSearch();
        }
    }

    //Popup for when the search didn't have results
    private void invalidSearch() {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Invalid Search");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Invalid search term. Please try again.");
        Button okButton = new Button("Okay");

        okButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    //Task to have creating a video in a new thread
    public void startTaskCreate()
    {
        String name = nameInput.getText();
        String lines = lineInput.getText();
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                Process nameProcess = scripts.getScript("nameValid", new String[] {name});
                Process lineProcess = scripts.getScript("lineValid", new String[] {searchTerm, lines});
                if(nameProcess.exitValue() == 1) {
                    return 1;
                }
                if(lineProcess.exitValue() == 1) {
                    return 2;
                }
                scripts.getScript("new", new String[] {searchTerm, lineInput.getText(), nameInput.getText()});
                return 0;
            }
        };
        task.setOnSucceeded(e -> {
            switch(task.getValue()) {
                case 0:
                    Alert success = new Alert(AlertType.CONFIRMATION, "Successfully created " + name, ButtonType.OK);
                    success.showAndWait();
                    nameInput.setText("");
                    lineInput.setText("");
                    searchInput.setText("");
                    rename = false;
                    hasTerm = false;
                    lineTable.getItems().clear();
                    break;
                case 1:
                    deleteOrRename(name);
                    break;
                case 2:
                    Alert lineWrong = new Alert(AlertType.WARNING, "You entered an invalid line number ("+lines+") please try again.", ButtonType.OK);
                    lineWrong.showAndWait();
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

    //Task for having wikit search in own thread
    public void startTaskSearch()
    {
        Task<ObservableList<Line>> task = new Task<ObservableList<Line>>() {
            @Override protected ObservableList<Line> call() throws Exception {
                Process process = scripts.getScript("search", new String[] {searchTerm});
                if(process.exitValue() == 1) {
                    return null;
                }
                hasTerm = true;
                ObservableList<Line> lineList = FXCollections.observableArrayList();
                try {
                    // Open the file
                    FileInputStream fstream = new FileInputStream(searchTerm+".text");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

                    String strLine;
                    //Read File Line By Line
                    int index=0;
                    while ((strLine = br.readLine()) != null)   {
                        // Print the content on the console
                        lineList.add(new Line(strLine, ++index));
                    }

                    //Close the input stream
                    fstream.close();

                }catch(Exception e) {

                }
                return lineList;
            }
        };
        task.setOnSucceeded(e -> {
            lineList = task.getValue();
            if(lineList == null) {
                invalidSearch();
            }else {
                lineTable.setItems(lineList);
                autoResizeColumns(lineTable);
            }
        });
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }
}