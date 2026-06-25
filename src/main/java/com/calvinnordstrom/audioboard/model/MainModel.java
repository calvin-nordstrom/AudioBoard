package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.AudioEngine;
import com.calvinnordstrom.audioboard.audio.AudioUtils;
import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.input.InputHandler;
import com.calvinnordstrom.audioboard.input.InputRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainModel {
    private final List<Sound> sounds = new ArrayList<>();
    private final AudioEngine audioEngine;
    private final InputRouter inputRouter;
    private final InputHandler inputHandler;

    public MainModel() {
        // Temporarily hardcoded; fill in your own file paths
        sounds.add(new Sound("Test1",
                null,
                null,
                new InputBinding(Input.Source.DESKTOP, "O"),
                0.8f,
                true
        ));
        sounds.add(new Sound("Test2",
                null,
                null,
                new InputBinding(Input.Source.DESKTOP, "P"),
                0.8f,
                true
        ));
        sounds.add(new Sound("Test3",
                null,
                null,
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

    public List<Sound> getSounds() {
        return sounds;
    }
}
