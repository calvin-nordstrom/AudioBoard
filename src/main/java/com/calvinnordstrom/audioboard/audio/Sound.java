package com.calvinnordstrom.audioboard.audio;

import com.calvinnordstrom.audioboard.input.InputBinding;

import java.io.*;

public class Sound implements Serializable {
    private String name;
    private File iconFile;
    private File soundFile;
    private InputBinding inputBinding;
    private float volume;
    private boolean enabled;
    private transient SoundAsset soundAsset;

    public Sound(
            String name,
            File iconFile,
            File soundFile,
            InputBinding inputBinding,
            float volume,
            boolean enabled
    ) {
        this.name = name;
        this.iconFile = iconFile;
        this.soundFile = soundFile;
        this.inputBinding = inputBinding;
        this.volume = volume;
        this.enabled = enabled;

        reloadSound();
    }

    public void reloadSound() {
        soundAsset = soundFile == null ? null : SoundLoader.load(soundFile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getIconFile() {
        return iconFile;
    }

    public void setIconFile(File iconFile) {
        this.iconFile = iconFile;
    }

    public File getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(File soundFile) {
        this.soundFile = soundFile;
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

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        reloadSound();
    }
}
