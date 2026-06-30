package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.viewmodel.SoundListViewModel;
import com.calvinnordstrom.audioboard.viewmodel.SoundViewModel;
import javafx.scene.Node;
import javafx.scene.control.ListView;

public class SoundListView {
    private final ListView<SoundViewModel> sounds = new ListView<>();

    public SoundListView(SoundListViewModel model) {
        sounds.setItems(model.getSounds());
        sounds.setCellFactory(_ -> new SoundCell());

        model.selectedSoundProperty().bind(sounds.getSelectionModel().selectedItemProperty());
        sounds.getSelectionModel().select(0);
    }

    public Node asNode() {
        return sounds;
    }
}
