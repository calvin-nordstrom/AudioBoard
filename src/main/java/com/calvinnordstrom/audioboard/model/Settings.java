package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.input.InputBinding;

public class Settings {
    private InputBinding stopSoundsBinding;
    private boolean localPlaybackEnabled;

    public Settings(
            InputBinding stopSoundsBinding,
            boolean localPlaybackEnabled
    ) {
        this.stopSoundsBinding = stopSoundsBinding;
        this.localPlaybackEnabled = localPlaybackEnabled;
    }

    public InputBinding getStopSoundsBinding() {
        return stopSoundsBinding;
    }

    public void setStopSoundsBinding(InputBinding stopSoundsBinding) {
        this.stopSoundsBinding = stopSoundsBinding;
    }

    public boolean isLocalPlaybackEnabled() {
        return localPlaybackEnabled;
    }

    public void setLocalPlaybackEnabled(boolean localPlaybackEnabled) {
        this.localPlaybackEnabled = localPlaybackEnabled;
    }
}
