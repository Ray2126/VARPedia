package varpedia.create;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import varpedia.helper.Scripts;
import varpedia.helper.Styling;

/**
 * The screen where the user can choose the chunks they want in their creation.
 *
 */
public class ChunkEditorScreen extends BorderPane{
	
	/**
	 * The top half of chunk screen.
	 */
    private ChunkTextViewer textSection;
    
    /**
     * The bottom half of chunk screen.
     */
    private ChunkTable tableSection;

    /**
     * Constructor.
     */
    public ChunkEditorScreen(){
    	Styling.yellowBG(this);
    	tableSection = new ChunkTable();
        textSection = new ChunkTextViewer(tableSection);

        BorderPane.setAlignment(textSection, Pos.TOP_CENTER);
        
        this.setTop(textSection);
        this.setBottom(tableSection.getMainPane());
    }

    /**
     * Get the top half of the chunk screen, the text area, voice and buttons.
     * @return	the text area, voice and buttons
     */
    public ChunkTextViewer getTextSection() {
        return textSection;
    }
    
    /**
     * Get the bottom half of the chunk screen, the table.
     * @return	the chunk audio table
     */
    public ChunkTable getVoiceSection() {
        return tableSection;
    }

    /**
     * Check they have at least one chunk saved.
     * @return false  no chunks saved
     * 		   true   at least one chunk saved
     */
    public boolean anyChunksCreated(){
        return tableSection.anyChunksCreated();
    }

    /**
     * Combining the audio chunks and music. Then once that is complete, make the preview creation.
     * @param mainScreen	the creation stage
     * @param music			the music the user selected
     */
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
               //TODO error handling
            }else {
                mainScreen.getPreview().makePreview(mainScreen, mainScreen.getImageAmount());
            }
        });
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
}