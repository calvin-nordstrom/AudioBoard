package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.AudioEngine;
import com.calvinnordstrom.audioboard.audio.AudioUtils;
import com.calvinnordstrom.audioboard.audio.SoundDefinition;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.input.InputHandler;
import com.calvinnordstrom.audioboard.input.InputRouter;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Paths;
import java.util.function.Consumer;

public class MainModel {
    private final ObservableList<SoundDefinition> sounds;
    private final AudioEngine audioEngine;
    private final InputRouter inputRouter;
    private final InputHandler inputHandler;

    public MainModel() {
        // Replace with file loading
        sounds = FXCollections.observableArrayList(
                sound -> new Observable[] {
                        sound.nameProperty(),
                        sound.iconPathProperty(),
                        sound.soundPathProperty(),
                        sound.inputBindingProperty(),
                        sound.volumeProperty(),
                        sound.enabledProperty()
                });
        // Temporarily hardcoded; fill in your own file paths
        sounds.add(new SoundDefinition("Test1",
                Paths.get(""),
                Paths.get(""),
                new InputBinding(Input.Source.DESKTOP, "O"),
                0.8f,
                true
        ));
        sounds.add(new SoundDefinition("Test2",
                null,
                Paths.get(""),
                new InputBinding(Input.Source.DESKTOP, "P"),
                0.8f,
                true
        ));
        sounds.add(new SoundDefinition("Test3",
                Paths.get(""),
                Paths.get(""),
                new InputBinding(Input.Source.KEYPAD, "0"),
                0.8f,
                true
        ));

        audioEngine = new AudioEngine(
                AudioUtils.getDefaultTarget(),
                AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)")
        );
        inputRouter = new InputRouter(sounds, audioEngine::submit);
        inputHandler = new InputHandler();
        inputHandler.addListener(inputRouter::route);
    }

    public void start() {
        audioEngine.start();
        inputHandler.start();
    }

    public void stop() {
        audioEngine.stop();
        inputHandler.stop();
    }

    public void addInputListener(Consumer<Input> listener) {
        inputHandler.addListener(listener);
    }

    public void removeInputListener(Consumer<Input> listener) {
        inputHandler.removeListener(listener);
    }

    public ObservableList<SoundDefinition> getSounds() {
        return sounds;
    }
}
