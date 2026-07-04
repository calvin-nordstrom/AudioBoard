package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.input.InputBinding;

import java.io.Serializable;

public class Settings implements Serializable {
    private InputBinding stopSoundsBinding;
    private boolean localPlaybackEnabled;
    private String inputDevice;
    private String outputDevice;

    public Settings(
            InputBinding stopSoundsBinding,
            boolean localPlaybackEnabled,
            String inputDevice,
            String outputDevice
    ) {
        this.stopSoundsBinding = stopSoundsBinding;
        this.localPlaybackEnabled = localPlaybackEnabled;
        this.inputDevice = inputDevice;
        this.outputDevice = outputDevice;
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

    public String getInputDevice() {
        return inputDevice;
    }

    public void setInputDevice(String inputDevice) {
        this.inputDevice = inputDevice;
    }

    public String getOutputDevice() {
        return outputDevice;
    }

    public void setOutputDevice(String outputDevice) {
        this.outputDevice = outputDevice;
    }
}
