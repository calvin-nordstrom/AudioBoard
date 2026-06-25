package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.view.control.BooleanControl;
import com.calvinnordstrom.audioboard.view.control.FloatControl;
import com.calvinnordstrom.audioboard.view.control.PathControl;
import com.calvinnordstrom.audioboard.view.control.StringControl;
import com.calvinnordstrom.audioboard.viewmodel.SoundListViewModel;
import com.calvinnordstrom.audioboard.viewmodel.SoundViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class SoundEditorView {
    private final SoundListViewModel model;
    private final MainController controller;
    private final VBox view = new VBox();

    public SoundEditorView(SoundListViewModel model, MainController controller) {
        this.model = model;
        this.controller = controller;
        model.selectedSoundProperty().addListener((_, _, newValue) -> setSound(newValue));

        controller.addInputListener(this::handleInput);
    }

    private void setSound(SoundViewModel sound) {
        view.getChildren().clear();

        if (sound == null) {
            view.getChildren().add(new Label("Select a sound"));
            return;
        }

        Label inputBindingLabel = new Label("Input Binding");
        Button inputBindingButton = new Button();
        inputBindingButton.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            if (sound.waitingForInputProperty().get()) {
                                return "Press any key...";
                            }

                            InputBinding binding = sound.inputBindingProperty().get();
                            if (binding == null) {
                                return "Unbound";
                            }

                            return binding.key() + " (" + binding.source() + ")";
                        },
                        sound.inputBindingProperty(),
                        sound.waitingForInputProperty()
                )
        );
        inputBindingButton.setOnMousePressed(e -> {
            sound.beginRebinding();
        });

        view.getChildren().addAll(
                new StringControl(
                        "Name",
                        sound.nameProperty()
                ).asNode(),

                new PathControl(
                        "Icon",
                        sound.iconPathProperty(),
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
                ).asNode(),

                new PathControl(
                        "Sound",
                        sound.soundPathProperty(),
                        new FileChooser.ExtensionFilter("WAV Files", "*.wav")
                ).asNode(),

                new VBox(inputBindingLabel, inputBindingButton),

                new FloatControl(
                        "Volume",
                        sound.volumeProperty(),
                        0,
                        100,
                        "%.0f%%",
                        v -> v * 100.0,
                        v -> v / 100.0
                ).asNode(),

                new BooleanControl(
                        "Enabled",
                        sound.enabledProperty()
                ).asNode()
        );
    }

    private void handleInput(Input input) {
        SoundViewModel sound = model.selectedSoundProperty().get();

        if (sound == null) {
            return;
        }

        if (!sound.waitingForInputProperty().get()) {
            return;
        }

        Platform.runLater(() -> {
            sound.finishRebinding(input);
        });
    }

    public Node asNode() {
        return view;
    }
}
