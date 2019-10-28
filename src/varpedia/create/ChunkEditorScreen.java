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
public class ChunkEditorScreen{
	
	private BorderPane chunkScreen;
	
	/**
	 * The top half of chunk screen where user selects text.
	 */
    private ChunkTextViewer textSection;
    
    /**
     * The bottom half of chunk screen where selected text is saved.
     */
    private ChunkTable tableSection;


    public ChunkEditorScreen(){
    	chunkScreen = new BorderPane();
    	tableSection = new ChunkTable();
        textSection = new ChunkTextViewer(tableSection);
        
    	Styling.yellowBG(chunkScreen);
        BorderPane.setAlignment(textSection.getScreen(), Pos.TOP_CENTER);
        
        chunkScreen.setTop(textSection.getScreen());
        chunkScreen.setBottom(tableSection.getMainPane());
    }
    
    public BorderPane getScreen() {
    	return chunkScreen;
    }

    public ChunkTextViewer getTextSection() {
        return textSection;
    }
    
    public ChunkTable getChunkTable() {
    	return tableSection;
    }

    /**
     * Check they have at least one chunk saved.
     * @return false when no chunks saved, true when at least one chunk saved
     */
    public boolean anyChunksCreated(){
        return tableSection.anyChunksCreated();
    }

    /**
     * Combining the audio chunks and music. Then once that is complete, make the preview creation.
     * @param mainScreen 	the creation stage
     * @param music 	the music the user selected
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
        	mainScreen.getPreview().makePreview(mainScreen, mainScreen.getImageAmount());
        });
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
}