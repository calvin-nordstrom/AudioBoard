package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class StringControl {
    private final String title;
    private final StringProperty valueProperty;
    private final VBox view = new VBox();

    public StringControl(String title, StringProperty valueProperty) {
        this.title = Objects.requireNonNull(title);
        this.valueProperty = Objects.requireNonNull(valueProperty);

        init();
    }

    private void init() {
        Label titleLabel = new Label(title);

        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(valueProperty);
        HBox textFieldPane = new HBox(textField);

        view.getChildren().addAll(titleLabel, textFieldPane);

        // Styles

        HBox.setHgrow(textField, Priority.ALWAYS);
    }

    public Node asNode() {
        return view;
    }
}
