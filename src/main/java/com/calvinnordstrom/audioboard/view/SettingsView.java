package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.view.control.BooleanControl;
import com.calvinnordstrom.audioboard.viewmodel.SettingsViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsView {
    private final SettingsViewModel model;
    private final MainController controller;
    private final HBox view = new HBox();

    public SettingsView(SettingsViewModel model, MainController controller) {
        this.model = model;
        this.controller = controller;

        controller.addInputListener(this::handleInput);

        init();
    }

    private void init() {
        view.getChildren().clear();

        Label stopSoundsBindingLabel = new Label("Stop Sounds Binding");
        Button stopSoundsBindingButton = new Button();
        stopSoundsBindingButton.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            if (model.waitingForInputProperty().get()) {
                                return "Press any key...";
                            }

                            InputBinding binding = model.stopSoundsBindingProperty().get();
                            if (binding == null) {
                                return "Unbound";
                            }

                            return binding.key() + " (" + binding.source() + ")";
                        },
                        model.stopSoundsBindingProperty(),
                        model.waitingForInputProperty()
                )
        );
        stopSoundsBindingButton.setOnMousePressed(e -> {
            model.beginRebinding();
        });
        VBox stopSoundsBindingVBox = new VBox(stopSoundsBindingLabel, stopSoundsBindingButton);

        BooleanControl localPlaybackControl = new BooleanControl(
                "Local Playback",
                model.localPlaybackEnabledProperty()
        );

        view.getChildren().addAll(
                stopSoundsBindingVBox,
                localPlaybackControl.asNode()
        );
    }

    private void handleInput(Input input) {
        if (!model.waitingForInputProperty().get()) {
            return;
        }

        Platform.runLater(() -> {
            model.finishRebinding(input);
        });
    }

    public Node asNode() {
        return view;
    }
}
