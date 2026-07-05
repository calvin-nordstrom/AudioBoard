package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.*;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputHandler;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

public class MainModel {
    private final DataModel dataModel;
    private final PersistenceManager persistenceManager = new PersistenceManager(
            Duration.ofSeconds(1)
    );
    private final AudioEngine virtualEngine;
    private final AudioEngine playbackEngine;
    private final AudioEngine localEngine;
    private final InputRouter inputRouter;
    private final InputHandler inputHandler;
    private final Consumer<Change<?>> changeHandler = this::onChanged;

    public MainModel() {
        dataModel = persistenceManager.load();

        virtualEngine = new AudioEngine(
                new AudioMixer(
                        AudioUtils.getTargetByName(dataModel.getSettings().getInputDevice()),
                        AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)")
                )
        );

        localEngine = new AudioEngine(
                new LocalAudioMixer(
                        AudioUtils.getSourceByName(dataModel.getSettings().getOutputDevice())
                )
        );

        playbackEngine = new AudioEngine(
                new LocalAudioMixer(
                        AudioUtils.getSourceByName(dataModel.getSettings().getOutputDevice())
                )
        );

        inputRouter = new InputRouter(
                dataModel.getSounds(),
                dataModel.getSettings(),
                virtualEngine,
                localEngine
        );

        inputHandler = new InputHandler();
        inputHandler.addListener(inputRouter::route);
    }

    public void start() {
        virtualEngine.start();
        localEngine.start();
        playbackEngine.start();
        inputHandler.start();
    }

    public void stop() {
        virtualEngine.stop();
        localEngine.stop();
        playbackEngine.stop();
        inputHandler.stop();
        persistenceManager.stop();
        persistenceManager.save(dataModel);
    }

    private void onChanged(Change<?> change) {
        persistenceManager.notifyChanged(dataModel);
    }

    public void addInputListener(Consumer<Input> listener) {
        inputHandler.addListener(listener);
    }

    public void removeInputListener(Consumer<Input> listener) {
        inputHandler.removeListener(listener);
    }

    public List<Sound> getSounds() {
        return dataModel.getSounds();
    }

    public Settings getSettings() {
        return dataModel.getSettings();
    }

    public AudioEngine getVirtualEngine() {
        return virtualEngine;
    }

    public AudioEngine getLocalEngine() {
        return localEngine;
    }

    public AudioEngine getPlaybackEngine() {
        return playbackEngine;
    }

    public Consumer<Change<?>> getChangeHandler() {
        return changeHandler;
    }
}
