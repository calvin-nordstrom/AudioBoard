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
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.nio.file.Path;

import static com.calvinnordstrom.audioboard.util.Resources.getImage;

public class SoundEditorView {
    private final SoundListViewModel model;
    private final MainController controller;
    private final VBox view = new VBox();
    private final ScrollPane scrollPane = new ScrollPane(view);

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
        scrollPane.getStyleClass().add("sound-editor-view-scroll-pane");
    }

    private void setSound(SoundViewModel sound) {
        view.getChildren().clear();

        if (sound == null) {
            view.getChildren().add(new Label("Select a sound"));
            return;
        }

        ImageView iconImage = new ImageView();
        iconImage.imageProperty().unbind();
        ObjectProperty<Path> iconPath = sound.iconPathProperty();
        iconImage.setImage(getImage(iconPath.get()));
        iconPath.addListener((_, _, newValue) -> iconImage.setImage(getImage(newValue)));
        HBox iconPane = new HBox(iconImage);

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
        inputBindingButton.setOnMousePressed(_ -> sound.beginRebinding());
        VBox inputBindingControlsPane = new VBox(inputBindingLabel, inputBindingButton);

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

        Button playButton = new Button("Play");
        playButton.setOnMousePressed(_ -> sound.playSound());
        Button stopButton = new Button("Stop");
        stopButton.setOnMousePressed(_ -> sound.stopSound());
        HBox playbackControlsPane = new HBox(playButton, stopButton);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnMousePressed(_ -> onDelete(sound));
        HBox deletePane = new HBox(deleteButton);

        view.getChildren().addAll(
                iconPane,
                nameControl.asNode(),
                iconControl.asNode(),
                soundControl.asNode(),
                inputBindingControlsPane,
                volumeControl.asNode(),
                enabledControl.asNode(),
                playbackControlsPane,
                deletePane
        );

        // Styles

        iconImage.setFitWidth(200);
        iconImage.setFitHeight(200);
        iconImage.setSmooth(true);

        HBox.setHgrow(iconPane, Priority.ALWAYS);

        playButton.setMaxWidth(Double.MAX_VALUE);
        stopButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(playButton, Priority.ALWAYS);
        HBox.setHgrow(stopButton, Priority.ALWAYS);

        HBox.setHgrow(deletePane, Priority.ALWAYS);

        iconPane.getStyleClass().add("sound-editor-view-icon-pane");
        playbackControlsPane.getStyleClass().add("sound-editor-view-playback-controls-pane");
        deleteButton.getStyleClass().add("delete-button");
        deletePane.getStyleClass().add("sound-editor-view-delete-pane");
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

    private void onDelete(SoundViewModel sound) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete \"" + sound.nameProperty().get() + "\"?"
        );
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(_ -> removeSound(sound));
    }

    private void removeSound(SoundViewModel sound) {
        model.getSounds().remove(sound);

        if (!model.getSounds().isEmpty()) {
            model.selectFirstSound();
        } else {
            setSound(null);
        }
    }

    public Node asNode() {
        return scrollPane;
    }
}
