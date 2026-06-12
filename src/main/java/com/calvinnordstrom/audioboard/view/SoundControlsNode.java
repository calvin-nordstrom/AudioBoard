package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.audio.SoundDefinition;
import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.util.Resources;
import com.calvinnordstrom.audioboard.view.control.BooleanControl;
import com.calvinnordstrom.audioboard.view.control.FloatControl;
import com.calvinnordstrom.audioboard.view.control.PathControl;
import com.calvinnordstrom.audioboard.view.control.StringControl;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.nio.file.Path;
import java.util.function.Consumer;

public class SoundControlsNode {
    private final MainController controller;
    private final SoundDefinition sound;
    private final VBox view = new VBox();
    private final Consumer<Input> listener;
    private volatile boolean waitingForInput = false;

    public SoundControlsNode(MainController controller, SoundDefinition sound) {
        this.controller = controller;
        this.sound = sound;

        listener = this::handleInput;
        controller.addInputListener(listener);

        init();
    }

    private void init() {
        Label title = new Label("Sound Editor");

        // Icon display
        ObjectProperty<Path> iconPath = sound.iconPathProperty();
        ImageView imageView = new ImageView(Resources.getImage(iconPath.get()));
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setSmooth(true);
        iconPath.addListener((_, _, newValue) -> imageView.setImage(Resources.getImage(newValue)));

        // Name control
        StringControl nameControl = new StringControl("Name", sound.nameProperty());

        // Icon path control
        FileChooser.ExtensionFilter iconFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp");
        PathControl iconPathControl = new PathControl("Icon file", sound.iconPathProperty(), iconFilter);

        // Sound path control
        FileChooser.ExtensionFilter soundFilter = new FileChooser.ExtensionFilter("WAV Files", "*.wav");
        PathControl soundPathControl = new PathControl("Sound file", sound.soundPathProperty(), soundFilter);

        // Input binding control
        ObjectProperty<InputBinding> inputBinding = sound.inputBindingProperty();
        Label inputBindingLabel = new Label("Input binding");
        Button inputBindingButton = new Button(inputBinding.get().key() + " (" + inputBinding.get().source() + ")");
        inputBindingButton.setOnMousePressed(_ -> {
            inputBindingButton.setText("Waiting for input...");
            inputBindingButton.setDisable(true);
            waitingForInput = true;
        });
        inputBinding.addListener((_, _, newValue) -> {
            inputBindingButton.setText(newValue.key() + " (" + newValue.source() + ")");
            inputBindingButton.setDisable(false);
        });
        VBox inputBindingVBox = new VBox(inputBindingLabel, inputBindingButton);

        // Volume control
        FloatControl volumeControl = new FloatControl(
                "Volume",
                sound.volumeProperty(),
                0,
                100,
                "%.0f%%",
                value -> value * 100.0,
                value -> value / 100.0
        );

        // Enabled control
        BooleanControl enabledControl = new BooleanControl("Enabled", sound.enabledProperty());

        view.getChildren().addAll(
                title,
                imageView,
                nameControl.asNode(),
                iconPathControl.asNode(),
                soundPathControl.asNode(),
                inputBindingVBox,
                volumeControl.asNode(),
                enabledControl.asNode()
        );
    }

    public void dispose() {
        controller.removeInputListener(listener);
    }

    private void handleInput(Input input) {
        if (!waitingForInput) {
            return;
        }

        Platform.runLater(() -> {
            if (!waitingForInput) {
                return;
            }

            InputBinding binding = new InputBinding(input.source(), input.key());
            sound.setInputBinding(binding);

            waitingForInput = false;
        });
    }

    public Node asNode() {
        return view;
    }
}
