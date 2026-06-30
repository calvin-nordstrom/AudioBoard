package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.view.control.BooleanControl;
import com.calvinnordstrom.audioboard.view.control.FloatRangeControl;
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

        init();
    }

    private void init() {
        // Styles

        view.getStyleClass().add("sound-editor-view");
    }

    private void setSound(SoundViewModel sound) {
        view.getChildren().clear();

        if (sound == null) {
            view.getChildren().add(new Label("Select a sound"));
            return;
        }

        StringControl nameControl = new StringControl(
                "Name",
                sound.nameProperty()
        );

        PathControl iconControl = new PathControl(
                "Icon",
                sound.iconPathProperty(),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        PathControl soundControl = new PathControl(
                "Sound",
                sound.soundPathProperty(),
                new FileChooser.ExtensionFilter("WAV Files", "*.wav")
        );

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
        VBox inputBindingVBox = new VBox(inputBindingLabel, inputBindingButton);

        FloatRangeControl volumeControl = new FloatRangeControl(
                "Volume",
                sound.volumeProperty(),
                0,
                100,
                "%.0f%%",
                v -> v * 100.0,
                v -> v / 100.0
        );

        BooleanControl enabledControl = new BooleanControl(
                "Enabled",
                sound.enabledProperty()
        );

        view.getChildren().addAll(
                nameControl.asNode(),
                iconControl.asNode(),
                soundControl.asNode(),
                inputBindingVBox,
                volumeControl.asNode(),
                enabledControl.asNode()
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
