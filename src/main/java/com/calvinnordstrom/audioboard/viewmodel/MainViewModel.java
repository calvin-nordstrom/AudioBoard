package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.model.Change;
import com.calvinnordstrom.audioboard.model.MainModel;

import java.util.function.Consumer;

public class MainViewModel {
    private final MainModel model;
    private final Consumer<Change<?>> onChanged;
    private final SoundListViewModel sounds;
    private final SettingsViewModel settings;

    public MainViewModel(MainModel model, Consumer<Change<?>> onChanged) {
        this.model = model;
        this.onChanged = onChanged;

        sounds = new SoundListViewModel(model.getSounds(), model.getPlaybackEngine(), onChanged);
        settings = new SettingsViewModel(model.getSettings(), onChanged);
    }

    public SoundListViewModel getSounds() {
        return sounds;
    }

    public SettingsViewModel getSettings() {
        return settings;
    }
}
