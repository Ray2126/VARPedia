package varpedia.creating;

import javafx.scene.layout.BorderPane;

// Screen that contains TextViewer and VoiceViewer
public class ChunkEditorScreen {
    BorderPane screen;
    TextViewer textSection;
    VoiceViewer voiceSection;
    public ChunkEditorScreen(){
        textSection = new TextViewer();
        voiceSection = new VoiceViewer();
        screen = new BorderPane();
        screen.setTop(textSection.getView());
    }
    public BorderPane getScreen(){
        return screen;
    }
}
