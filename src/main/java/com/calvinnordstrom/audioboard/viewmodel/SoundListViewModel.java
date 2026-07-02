package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.audio.AudioEngine;
import com.calvinnordstrom.audioboard.audio.Sound;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.List;

public class SoundListViewModel {
    private final List<Sound> model;
    private final AudioEngine audioEngine;
    private final ObservableList<SoundViewModel> sounds = FXCollections.observableArrayList();
    private final ObjectProperty<SoundViewModel> selectedSound = new SimpleObjectProperty<>();

    public SoundListViewModel(List<Sound> model, AudioEngine audioEngine) {
        this.model = model;
        this.audioEngine = audioEngine;

        for (Sound sound : model) {
            this.sounds.add(new SoundViewModel(sound, audioEngine));
        }

        bindBackToModel();
    }

    private void bindBackToModel() {
        sounds.addListener((ListChangeListener<SoundViewModel>) c -> {
            model.clear();
            for (SoundViewModel sound : sounds) {
                model.add(sound.getModel());
            }
        });
    }

    public ObservableList<SoundViewModel> getSounds() {
        return sounds;
    }

    public ObjectProperty<SoundViewModel> selectedSoundProperty() {
        return selectedSound;
    }

    public void selectSound(int index) {
        selectedSound.set(sounds.get(index));
    }

    public void selectFirstSound() {
        selectedSound.set(sounds.getFirst());
    }

    public void selectLastSound() {
        selectedSound.set(sounds.getLast());
    }

    public void addSoundByPath(Path soundPath) {
        Sound sound = new Sound(
                "New Sound",
                null,
                soundPath,
                null,
                0.8f,
                true
        );

        sounds.add(new SoundViewModel(sound, audioEngine));
    }
}
