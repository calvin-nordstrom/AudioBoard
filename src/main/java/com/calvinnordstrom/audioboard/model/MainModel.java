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
                        new JavaSoundInput(AudioUtils.getTargetByName(dataModel.getSettings().getInputDevice())),
                        new JavaSoundOutput(AudioUtils.getSourceByName("CABLE Input (VB-Audio Virtual Cable)"), 4096)
                )
        );

        localEngine = new AudioEngine(
                new LocalAudioMixer(
                        new JavaSoundOutput(AudioUtils.getSourceByName(dataModel.getSettings().getOutputDevice()), 4096)
                )
        );

        playbackEngine = new AudioEngine(
                new LocalAudioMixer(
                        new JavaSoundOutput(AudioUtils.getSourceByName(dataModel.getSettings().getOutputDevice()), 4096)
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
        persistenceManager.save(dataModel);

        persistenceManager.stop();
        virtualEngine.stop();
        localEngine.stop();
        playbackEngine.stop();
        inputHandler.stop();
    }

    private void onChanged(Change<?> change) {
        persistenceManager.notifyChanged(dataModel);

        if (change.property().equals("inputDevice")
                && change.source() instanceof String inputDevice)
        {
            virtualEngine.submit(
                    new ChangeInputCommand(
                            new JavaSoundInput(AudioUtils.getTargetByName(inputDevice))
                    )
            );
        }

        if (change.property().equals("outputDevice")
                && change.source() instanceof String outputDevice)
        {
            localEngine.submit(
                    new ChangeOutputCommand(
                            new JavaSoundOutput(AudioUtils.getSourceByName(outputDevice), 4096)
                    )
            );
            playbackEngine.submit(
                    new ChangeOutputCommand(
                            new JavaSoundOutput(AudioUtils.getSourceByName(outputDevice), 4096)
                    )
            );
        }
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
