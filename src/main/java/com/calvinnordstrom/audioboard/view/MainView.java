package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.viewmodel.SoundListViewModel;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainView {
    private final SoundListViewModel model;
    private final MainController controller;
    private final BorderPane view = new BorderPane();
    private final SoundListView soundListView;
    private final SoundEditorView soundEditorView;

    public MainView(SoundListViewModel model, MainController controller) {
        this.model = model;
        this.controller = controller;

        soundListView = new SoundListView(model);
        soundEditorView = new SoundEditorView(model, controller);

        view.setCenter(soundListView.asNode());
        view.setLeft(soundEditorView.asNode());
    }


    public Node asNode() {
        return view;
    }
}
