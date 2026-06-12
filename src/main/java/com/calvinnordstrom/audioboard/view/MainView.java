package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.audio.SoundDefinition;
import com.calvinnordstrom.audioboard.controller.MainController;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class MainView {
    private final MainController controller;
    private final ObservableList<SoundDefinition> sounds;
    private final BorderPane view = new BorderPane();
    private final Pane soundControlsPane = new Pane();
    private final FlowPane soundsPane = new FlowPane();
    private SoundControlsNode currentSoundControls;

    public MainView(MainController controller) {
        this.controller = controller;
        sounds = controller.getSounds();

        init();
        initSoundControlsPane();
        initSoundsPane();
    }

    private void init() {
    }

    private void initSoundControlsPane() {
        view.setLeft(soundControlsPane);
    }

    private void initSoundsPane() {
        renderSounds();
        sounds.addListener((ListChangeListener<? super SoundDefinition>) change -> {
            renderSounds();
        });

        view.setCenter(soundsPane);
    }

    private void renderSounds() {
        soundsPane.getChildren().clear();

        controller.getSounds().forEach(sound -> {
            SoundNode soundNode = new SoundNode(sound);
            soundsPane.getChildren().add(soundNode.asNode());
            soundNode.asNode().setOnMousePressed(_ -> {
                renderSoundControls(sound);
            });
        });
    }

    private void renderSoundControls(SoundDefinition sound) {
        if (currentSoundControls != null) {
            currentSoundControls.dispose();
        }

        soundControlsPane.getChildren().clear();

        currentSoundControls = new SoundControlsNode(controller, sound);
        soundControlsPane.getChildren().add(currentSoundControls.asNode());
    }

    public void dispose() {
        if (currentSoundControls != null) {
            currentSoundControls.dispose();
        }
    }

    public Node asNode() {
        return view;
    }
}
