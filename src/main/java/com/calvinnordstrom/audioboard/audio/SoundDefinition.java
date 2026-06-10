package com.calvinnordstrom.audioboard.audio;

import com.calvinnordstrom.audioboard.input.InputBinding;

import java.nio.file.Path;

public class SoundDefinition {
    private String name;
    private Path iconFile;
    private Path soundFile;
    private InputBinding inputBinding;
    private float volume;
    private boolean enabled;
    private transient SoundAsset soundAsset;

    public SoundDefinition(String name,
                           Path iconFile,
                           Path soundFile,
                           InputBinding inputBinding,
                           float volume,
                           boolean enabled) {
        this.name = name;
        this.iconFile = iconFile;
        this.soundFile = soundFile;
        this.inputBinding = inputBinding;
        this.volume = Math.clamp(volume, 0f, 1f);
        this.enabled = enabled;

        reloadSound();
    }

    public void reloadSound() {
        soundAsset = SoundLoader.load(soundFile.toFile());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getIconFile() {
        return iconFile;
    }

    public void setIconFile(Path iconFile) {
        this.iconFile = iconFile;
    }

    public Path getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(Path soundFile) {
        this.soundFile = soundFile;

        reloadSound();
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
        this.volume = Math.clamp(volume, 0f, 1f);
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
}
