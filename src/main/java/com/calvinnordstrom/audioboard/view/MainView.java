package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.viewmodel.MainViewModel;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainView {
    private final MainViewModel model;
    private final MainController controller;
    private final BorderPane view = new BorderPane();
    private final SoundEditorView soundEditorView;
    private final SettingsView settingsView;
    private final SoundListView soundListView;

    public MainView(MainViewModel model, MainController controller) {
        this.model = model;
        this.controller = controller;

        soundEditorView = new SoundEditorView(model.getSounds(), controller);
        settingsView = new SettingsView(model.getSettings(), controller);
        soundListView = new SoundListView(model.getSounds());

        init();
    }

    private void init() {
        model.getSounds().selectedSoundProperty().addListener((_, _, _) -> {
            view.requestFocus();
        });

        view.sceneProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                view.requestFocus();
            }
        });

        view.setLeft(soundEditorView.asNode());
        view.setBottom(settingsView.asNode());
        view.setCenter(soundListView.asNode());
    }

    public Node asNode() {
        return view;
    }
}
