package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class PathControl {
    private final String title;
    private final ObjectProperty<Path> valueProperty;
    private final FileChooser.ExtensionFilter filter;
    private final VBox view = new VBox();
    private final Tooltip fileLabelTooltip = new Tooltip();

    public PathControl(String title, ObjectProperty<Path> valueProperty, FileChooser.ExtensionFilter filter) {
        this.title = Objects.requireNonNull(title);
        this.valueProperty = Objects.requireNonNull(valueProperty);
        this.filter = Objects.requireNonNull(filter);

        init();
    }

    private void init() {
        Label titleLabel = new Label(title);

        Button fileButton = new Button("Choose file");
        fileButton.addEventHandler(MouseEvent.MOUSE_PRESSED, _ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open");
            fileChooser.getExtensionFilters().add(filter);
            Window owner = view.getScene() != null
                    ? view.getScene().getWindow()
                    : null;
            File selectedFile = fileChooser.showOpenDialog(owner);

            if (selectedFile != null) {
                valueProperty.set(selectedFile.toPath());
            }
        });

        Label fileLabel = new Label(valueProperty.get() == null
                ? "No file selected"
                : valueProperty.get().getFileName().toString()
        );
        fileLabelTooltip.setText(fileLabel.getText());

        HBox filePane = new HBox(fileButton, fileLabel);

        valueProperty.addListener((_, _, newValue) -> {
            String newName = newValue.getFileName().toString();
            fileLabel.setText(newName);
            fileLabelTooltip.setText(newName);
        });

        view.getChildren().addAll(titleLabel, filePane);
    }

    public Node asNode() {
        return view;
    }
}
