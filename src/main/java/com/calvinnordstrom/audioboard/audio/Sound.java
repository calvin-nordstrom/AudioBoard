package com.calvinnordstrom.audioboard.audio;

import com.calvinnordstrom.audioboard.input.InputBinding;

import java.nio.file.Path;

public class Sound {
    private String name;
    private Path iconPath;
    private Path soundPath;
    private InputBinding inputBinding;
    private float volume;
    private boolean enabled;
    private transient SoundAsset soundAsset;

    public Sound(
            String name,
            Path iconPath,
            Path soundPath,
            InputBinding inputBinding,
            float volume,
            boolean enabled
    ) {
        this.name = name;
        this.iconPath = iconPath;
        this.soundPath = soundPath;
        this.inputBinding = inputBinding;
        this.volume = volume;
        this.enabled = enabled;

        reloadSound();
    }

    public void reloadSound() {
        soundAsset = soundPath == null ? null : SoundLoader.load(soundPath.toFile());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getIconPath() {
        return iconPath;
    }

    public void setIconPath(Path iconPath) {
        this.iconPath = iconPath;
    }

    public Path getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(Path soundPath) {
        this.soundPath = soundPath;
    }

    public InputBinding getInputBinding() {
        return inputBinding;
    }

    public void setInputBinding(InputBinding inputBinding) {
        this.inputBinding = inputBinding;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SoundAsset getSoundAsset() {
        return soundAsset;
    }

    public void setSoundAsset(SoundAsset soundAsset) {
        this.soundAsset = soundAsset;
    }
}
