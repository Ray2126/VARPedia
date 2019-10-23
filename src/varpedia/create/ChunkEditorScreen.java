package varpedia.create;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

// Screen that contains TextViewer and VoiceViewer
public class ChunkEditorScreen {
    BorderPane screen;
    TextViewer textSection;
    public VoiceViewer voiceSection;

    public ChunkEditorScreen(){
        voiceSection = new VoiceViewer();
        textSection = new TextViewer(voiceSection);
        screen = new BorderPane();
        BorderPane.setAlignment(textSection.getView(), Pos.TOP_CENTER);
        screen.setTop(textSection.getView());
        screen.setBottom(voiceSection.getMainPane());
    }
    
    public BorderPane getScreen(){
        return screen;
    }

    public TextViewer getTextSection() {
        return textSection;
    }
    
    public VoiceViewer getVoiceSection() {
        return voiceSection;
    }

    public boolean anySelected(){
        return voiceSection.anySelected();
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
