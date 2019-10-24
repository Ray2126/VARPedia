package varpedia.create;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

public class ChunkEditorScreen extends BorderPane{
	
    private ChunkTextViewer textSection;
    private ChunkTable tableSection;

    public ChunkEditorScreen(){
    	Styling.yellowBG(this);
    	tableSection = new ChunkTable();
        textSection = new ChunkTextViewer(tableSection);

        BorderPane.setAlignment(textSection, Pos.TOP_CENTER);
        
        this.setTop(textSection);
        this.setBottom(tableSection.getMainPane());
    }

    public ChunkTextViewer getTextSection() {
        return textSection;
    }
    
    public ChunkTable getVoiceSection() {
        return tableSection;
    }

    public boolean anySelected(){
        return tableSection.anySelected();
    }

    public void combineTheAudio(CreatorMain mainScreen, String music) {
        Scripts scripts = new Scripts();
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                Process process = scripts.getScript("mergeAudio", new String[]{music});
                return process.exitValue();
            }
        };
        task.setOnSucceeded(e -> {
            int result = task.getValue();
            if(result != 0) {
               //error
            }else {
                //Should be images but not done yet
                mainScreen.getPreview().audioAndImageCombine(mainScreen, mainScreen.getImageAmount());
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