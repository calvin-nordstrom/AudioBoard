package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.view.control.PathControl;
import com.calvinnordstrom.audioboard.viewmodel.SoundListViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.nio.file.Path;

import static com.calvinnordstrom.audioboard.util.Resources.getResource;

public class SoundCreatorView {
    private static final String TITLE = "Upload Sound";
    private final SoundListViewModel model;
    private final Window owner;
    private final VBox view = new VBox();
    private final Scene scene = new Scene(view);
    private final Stage stage = new Stage();

    public SoundCreatorView(SoundListViewModel model, Window owner) {
        this.model = model;
        this.owner = owner;

        init();
    }

    private void init() {
        scene.getStylesheets().add(getResource("css/styles.css"));

        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.setResizable(false);

        ObjectProperty<Path> soundPathProperty = new SimpleObjectProperty<>();
        PathControl soundControl = new PathControl(
                "Sound",
                soundPathProperty,
                new FileChooser.ExtensionFilter("WAV Files", "*.wav")
        );

        Button okButton = new Button("OK");
        okButton.setDisable(true);
        okButton.setOnMousePressed(_ -> {
            model.addSoundByPath(soundPathProperty.getValue());
            model.selectLastSound();
            hide();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnMousePressed(_ -> {
            hide();
        });
        HBox controlsPane = new HBox(okButton, cancelButton);

        soundPathProperty.addListener((_, _, newValue) -> {
            if (newValue != null) {
                okButton.setDisable(false);
            }
        });

        view.getChildren().addAll(
                soundControl.asNode(),
                controlsPane
        );

        // Styles

        okButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(okButton, Priority.ALWAYS);
        HBox.setHgrow(cancelButton, Priority.ALWAYS);

        view.getStyleClass().add("sound-creator-view");
        controlsPane.getStyleClass().add("sound-creator-view-controls-pane");
    }

    public void show() {
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public void hide() {
        stage.setScene(null);
        stage.hide();
    }
}
