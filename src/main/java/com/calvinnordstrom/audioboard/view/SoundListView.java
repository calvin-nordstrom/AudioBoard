package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.viewmodel.SoundListViewModel;
import com.calvinnordstrom.audioboard.viewmodel.SoundViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class SoundListView {
    private final FlowPane sounds = new FlowPane();
    private final ScrollPane scrollPane = new ScrollPane(sounds);

    public SoundListView(SoundListViewModel model) {
        rebuild(model);

        model.getSounds().addListener((ListChangeListener<SoundViewModel>) _ -> rebuild(model));
        model.selectFirstSound();

        init();
    }

    private void init() {
        // Styles

        sounds.getStyleClass().add("sound-list-view-flow-pane");
        scrollPane.getStyleClass().add("sound-list-view-scroll-pane");
    }

    private void rebuild(SoundListViewModel model) {
        sounds.getChildren().clear();

        for (SoundViewModel sound : model.getSounds()) {
            SoundCell cell = new SoundCell(sound, model);
            sounds.getChildren().add(cell.asNode());
        }
    }

    public Node asNode() {
        return scrollPane;
    }
}
