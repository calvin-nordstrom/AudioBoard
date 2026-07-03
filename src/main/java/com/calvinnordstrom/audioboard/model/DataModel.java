package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataModel implements Serializable {
    private List<Sound> sounds = new ArrayList<>();
    private Settings settings = new Settings(new InputBinding(Input.Source.DESKTOP, "Escape"), true);

    public DataModel() {
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
