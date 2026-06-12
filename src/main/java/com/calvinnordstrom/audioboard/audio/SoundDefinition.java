package com.calvinnordstrom.audioboard.audio;

import com.calvinnordstrom.audioboard.input.InputBinding;
import javafx.beans.property.*;

import java.nio.file.Path;

public class SoundDefinition {
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<Path> iconPath = new SimpleObjectProperty<>();
    private final ObjectProperty<Path> soundPath = new SimpleObjectProperty<>();
    private final ObjectProperty<InputBinding> inputBinding = new SimpleObjectProperty<>();
    private final FloatProperty volume = new SimpleFloatProperty();
    private final BooleanProperty enabled = new SimpleBooleanProperty();
    private transient SoundAsset soundAsset;

    public SoundDefinition(
            String name,
            Path iconPath,
            Path soundPath,
            InputBinding inputBinding,
            float volume,
            boolean enabled
    ) {
        this.soundPath.addListener((_, _, newValue) -> {
            soundAsset = SoundLoader.load(newValue.toFile());
        });

        setName(name);
        setIconPath(iconPath);
        setSoundPath(soundPath);
        setInputBinding(inputBinding);
        setVolume(volume);
        setEnabled(enabled);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Path getIconPath() {
        return iconPath.get();
    }

    public void setIconPath(Path iconPath) {
        this.iconPath.set(iconPath);
    }

    public ObjectProperty<Path> iconPathProperty() {
        return iconPath;
    }

    public Path getSoundPath() {
        return soundPath.get();
    }

    public void setSoundPath(Path soundPath) {
        this.soundPath.set(soundPath);
    }

    public ObjectProperty<Path> soundPathProperty() {
        return soundPath;
    }

    public InputBinding getInputBinding() {
        return inputBinding.get();
    }

    public void setInputBinding(InputBinding inputBinding) {
        this.inputBinding.set(inputBinding);
    }

    public ObjectProperty<InputBinding> inputBindingProperty() {
        return inputBinding;
    }

    public float getVolume() {
        return volume.get();
    }

    public void setVolume(float volume) {
        this.volume.set(Math.clamp(volume, 0f, 1f));
    }

    public FloatProperty volumeProperty() {
        return volume;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public SoundAsset getSoundAsset() {
        return soundAsset;
    }
}
