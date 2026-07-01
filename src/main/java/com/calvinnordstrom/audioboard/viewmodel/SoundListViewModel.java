package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.audio.AudioEngine;
import com.calvinnordstrom.audioboard.audio.Sound;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SoundListViewModel {
    private final ObservableList<SoundViewModel> sounds = FXCollections.observableArrayList();
    private final ObjectProperty<SoundViewModel> selectedSound = new SimpleObjectProperty<>();

    public SoundListViewModel(List<Sound> sounds, AudioEngine audioEngine) {
        for (Sound sound : sounds) {
            this.sounds.add(new SoundViewModel(sound, audioEngine));
        }
    }

    public ObservableList<SoundViewModel> getSounds() {
        return sounds;
    }

    public ObjectProperty<SoundViewModel> selectedSoundProperty() {
        return selectedSound;
    }
}
