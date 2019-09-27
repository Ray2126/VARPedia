package varpedia.creating;

import javafx.scene.layout.BorderPane;

// Screen that contains TextViewer and VoiceViewer
public class ChunkEditorScreen {
    BorderPane screen;
    TextViewer textSection;
    VoiceViewer voiceSection;

    public ChunkEditorScreen(){
        voiceSection = new VoiceViewer();
        textSection = new TextViewer(voiceSection);
        screen = new BorderPane();
        screen.setTop(textSection.getView());
        screen.setBottom(voiceSection.getAudioList());
    }
    public BorderPane getScreen(){
        return screen;
    }

    public TextViewer getTextSection() {
        return textSection;
    }
}
