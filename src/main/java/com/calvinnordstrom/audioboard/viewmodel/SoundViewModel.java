package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.audio.AudioEngine;
import com.calvinnordstrom.audioboard.audio.command.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.audio.command.StopSampleCommand;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.model.Change;
import javafx.beans.property.*;

import java.io.File;
import java.util.function.Consumer;

public class SoundViewModel {
    private final Sound model;
    private final AudioEngine audioEngine;
    private final Consumer<Change<?>> onChanged;
    private final StringProperty name;
    private final ObjectProperty<File> iconFile;
    private final ObjectProperty<File> soundFile;
    private final ObjectProperty<InputBinding> inputBinding;
    private final FloatProperty volume;
    private final BooleanProperty enabled;
    private final BooleanProperty waitingForInput = new SimpleBooleanProperty(false);

    public SoundViewModel(Sound model, AudioEngine audioEngine, Consumer<Change<?>> onChanged) {
        this.model = model;
        this.audioEngine = audioEngine;
        this.onChanged = onChanged;

        name = new SimpleStringProperty(model.getName());
        iconFile = new SimpleObjectProperty<>(model.getIconFile());
        soundFile = new SimpleObjectProperty<>(model.getSoundFile());
        inputBinding = new SimpleObjectProperty<>(model.getInputBinding());
        volume = new SimpleFloatProperty(model.getVolume());
        enabled = new SimpleBooleanProperty(model.isEnabled());

        bindBackToModel();
    }

    private void bindBackToModel() {
        name.addListener((_, oldValue, newValue) -> {
            model.setName(newValue);
            onChanged.accept(new Change<>(model.getName(), "name", oldValue, newValue));
        });
        iconFile.addListener((_, oldValue, newValue) -> {
            model.setIconFile(newValue);
            onChanged.accept(new Change<>(model.getIconFile(), "iconFile", oldValue, newValue));
        });
        soundFile.addListener((_, oldValue, newValue) -> {
            model.setSoundFile(newValue);
            model.reloadSound();
            onChanged.accept(new Change<>(model.getSoundFile(), "soundFile", oldValue, newValue));
        });
        inputBinding.addListener((_, oldValue, newValue) -> {
            model.setInputBinding(newValue);
            onChanged.accept(new Change<>(model.getInputBinding(), "inputBinding", oldValue, newValue));
        });
        volume.addListener((_, oldValue, newValue) -> {
            model.setVolume(newValue.floatValue());
            onChanged.accept(new Change<>(model.getVolume(), "volume", oldValue, newValue));
        });
        enabled.addListener((_, oldValue, newValue) -> {
            model.setEnabled(newValue);
            onChanged.accept(new Change<>(model.isEnabled(), "enabled", oldValue, newValue));
        });
    }

    public void beginRebinding() {
        waitingForInput.set(true);
    }

    public void finishRebinding(Input input) {
        inputBinding.set(new InputBinding(input.source(), input.key()));
        waitingForInput.set(false);
    }

    public void playSound() {
        audioEngine.submit(new PlaySampleCommand(model));
    }

    public void stopSound() {
        audioEngine.submit(new StopSampleCommand(model));
    }

    Sound getModel() {
        return model;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<File> iconFileProperty() {
        return iconFile;
    }

    public ObjectProperty<File> soundFileProperty() {
        return soundFile;
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
