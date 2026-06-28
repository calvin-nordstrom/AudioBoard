package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.model.Settings;

import java.util.List;

public class MainViewModel {
    private final SoundListViewModel sounds;
    private final SettingsViewModel settings;

    public MainViewModel(
            List<Sound> sounds,
            Settings settings
    ) {
        this.sounds = new SoundListViewModel(sounds);
        this.settings = new SettingsViewModel(settings);
    }

    public SoundListViewModel getSounds() {
        return sounds;
    }

    public SettingsViewModel getSettings() {
        return settings;
    }
}
