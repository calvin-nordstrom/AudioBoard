package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import javafx.beans.property.*;

import java.nio.file.Path;

public class SoundViewModel {
    private final Sound model;
    private final StringProperty name;
    private final ObjectProperty<Path> iconPath;
    private final ObjectProperty<Path> soundPath;
    private final ObjectProperty<InputBinding> inputBinding;
    private final FloatProperty volume;
    private final BooleanProperty enabled;
    private final BooleanProperty waitingForInput = new SimpleBooleanProperty(false);

    public SoundViewModel(Sound model) {
        this.model = model;

        name = new SimpleStringProperty(model.getName());
        iconPath = new SimpleObjectProperty<>(model.getIconPath());
        soundPath = new SimpleObjectProperty<>(model.getSoundPath());
        inputBinding = new SimpleObjectProperty<>(model.getInputBinding());
        volume = new SimpleFloatProperty(model.getVolume());
        enabled = new SimpleBooleanProperty(model.isEnabled());

        bindBackToModel();
    }

    private void bindBackToModel() {
        name.addListener((_, _, newValue) -> model.setName(newValue));
        iconPath.addListener((_, _, newValue) -> model.setIconPath(newValue));
        soundPath.addListener((_, _, newValue) -> {
            model.setSoundPath(newValue);
            model.reloadSound();
        });
        inputBinding.addListener((_, _, newValue) -> model.setInputBinding(newValue));
        volume.addListener((_, _, newValue) -> model.setVolume(newValue.floatValue()));
        enabled.addListener((_, _, newValue) -> model.setEnabled(newValue));
    }

    public void beginRebinding() {
        waitingForInput.set(true);
    }

    public void finishRebinding(Input input) {
        inputBinding.set(new InputBinding(input.source(), input.key()));
        waitingForInput.set(false);
    }

    public Sound getModel() {
        return model;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<Path> iconPathProperty() {
        return iconPath;
    }

    public ObjectProperty<Path> soundPathProperty() {
        return soundPath;
    }

    public ObjectProperty<InputBinding> inputBindingProperty() {
        return inputBinding;
    }

    public FloatProperty volumeProperty() {
        return volume;
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public BooleanProperty waitingForInputProperty() {
        return waitingForInput;
    }
}
