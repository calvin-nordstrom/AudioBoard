package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.model.MainModel;

public class MainViewModel {
    private final MainModel model;
    private final SoundListViewModel sounds;
    private final SettingsViewModel settings;

    public MainViewModel(MainModel model) {
        this.model = model;
        this.sounds = new SoundListViewModel(model.getSounds(), model.getPlaybackEngine());
        this.settings = new SettingsViewModel(model.getSettings());
    }

    public SoundListViewModel getSounds() {
        return sounds;
    }

    public SettingsViewModel getSettings() {
        return settings;
    }
}
