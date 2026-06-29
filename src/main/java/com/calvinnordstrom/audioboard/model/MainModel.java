package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.*;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.input.InputHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainModel {
    private final List<Sound> sounds = new ArrayList<>();
    private final Settings settings = new Settings(new InputBinding(Input.Source.DESKTOP, "Space"), true);
    private final AudioEngine virtualEngine;
    private final AudioEngine localEngine;
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

        virtualEngine = new AudioEngine(
                new AudioMixer(
                        AudioUtils.getDefaultTarget(),
                        AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)")
                )
        );

        localEngine = new AudioEngine(
                new LocalAudioMixer(
                        AudioUtils.getSourceByName("Logitech PRO X Gaming Headset")
                )
        );

        inputRouter = new InputRouter(sounds, settings, virtualEngine, localEngine);
        inputHandler = new InputHandler();
        inputHandler.addListener(inputRouter::route);
    }

    public void start() {
        virtualEngine.start();
        localEngine.start();
        inputHandler.start();
    }

    public void stop() {
        virtualEngine.stop();
        localEngine.stop();
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

    public Settings getSettings() {
        return settings;
    }
}
