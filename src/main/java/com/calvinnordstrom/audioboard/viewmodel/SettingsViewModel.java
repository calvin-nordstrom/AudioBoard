package com.calvinnordstrom.audioboard.viewmodel;

import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.model.Settings;
import javafx.beans.property.*;

import java.util.function.Consumer;

public class SettingsViewModel {
    private final Settings model;
    private final Consumer<Object> onChanged;
    private final ObjectProperty<InputBinding> stopSoundsBinding;
    private final BooleanProperty localPlaybackEnabled;
    private final ObjectProperty<String> inputDevice;
    private final ObjectProperty<String> outputDevice;
    private final BooleanProperty waitingForInput = new SimpleBooleanProperty(false);

    public SettingsViewModel(Settings model, Consumer<Object> onChanged) {
        this.model = model;
        this.onChanged = onChanged;

        stopSoundsBinding = new SimpleObjectProperty<>(model.getStopSoundsBinding());
        localPlaybackEnabled = new SimpleBooleanProperty(model.isLocalPlaybackEnabled());
        inputDevice = new SimpleObjectProperty<>(model.getInputDevice());
        outputDevice = new SimpleObjectProperty<>(model.getOutputDevice());

        bindBackToModel();
    }

    private void bindBackToModel() {
        stopSoundsBinding.addListener((_, _, newValue) -> {
            model.setStopSoundsBinding(newValue);
            onChanged.accept(newValue);
        });
        localPlaybackEnabled.addListener((_, _, newValue) -> {
            model.setLocalPlaybackEnabled(newValue);
            onChanged.accept(newValue);
        });
        inputDevice.addListener((_, _, newValue) -> {
            model.setInputDevice(newValue);
            onChanged.accept(newValue);
        });
        outputDevice.addListener((_, _, newValue) -> {
            model.setOutputDevice(newValue);
            onChanged.accept(newValue);
        });
    }

    public void beginRebinding() {
        waitingForInput.set(true);
    }

    public void finishRebinding(Input input) {
        stopSoundsBinding.set(new InputBinding(input.source(), input.key()));
        waitingForInput.set(false);
    }

    public ObjectProperty<InputBinding> stopSoundsBindingProperty() {
        return stopSoundsBinding;
    }

    public BooleanProperty localPlaybackEnabledProperty() {
        return localPlaybackEnabled;
    }

    public ObjectProperty<String> inputDeviceProperty() {
        return inputDevice;
    }

    public ObjectProperty<String> outputDeviceProperty() {
        return outputDevice;
    }

    public BooleanProperty waitingForInputProperty() {
        return waitingForInput;
    }
}
