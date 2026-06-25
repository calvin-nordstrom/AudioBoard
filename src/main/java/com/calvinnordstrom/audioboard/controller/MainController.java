package com.calvinnordstrom.audioboard.controller;

import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.model.MainModel;

import java.util.List;
import java.util.function.Consumer;

public class MainController {
    private final MainModel model;

    public MainController(MainModel model) {
        this.model = model;
    }

    public void addInputListener(Consumer<Input> listener) {
        model.addInputListener(listener);
    }

    public void removeInputListener(Consumer<Input> listener) {
        model.removeInputListener(listener);
    }

    public List<Sound> getSounds() {
        return model.getSounds();
    }
}
