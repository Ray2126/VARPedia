package varpedia.creating;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import varpedia.CreatorMain;

// Screen that contains TextViewer and VoiceViewer
public class ChunkEditorScreen {
    BorderPane screen;
    TextViewer textSection;
    VoiceViewer voiceSection;

    public ChunkEditorScreen(){
        voiceSection = new VoiceViewer();
        textSection = new TextViewer(voiceSection);
        screen = new BorderPane();
        BorderPane.setAlignment(textSection.getView(), Pos.TOP_CENTER);
        screen.setTop(textSection.getView());
        screen.setBottom(voiceSection.getAudioList());
    }
    public BorderPane getScreen(){
        return screen;
    }

    public TextViewer getTextSection() {
        return textSection;
    }

    public void combineTheAudio(CreatorMain mainScreen) {
        Scripts scripts = new Scripts();
        Task<Integer> task = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                Process process = scripts.getScript("search", new String[]{});
                return process.exitValue();
            }
        };
        task.setOnSucceeded(e -> {
            int result = task.getValue();
            if(result != 0) {
               //error
            }else {
                //Should be images but not done yet
                mainScreen.createScreenUp();
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
