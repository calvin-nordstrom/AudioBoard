package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class BooleanControl {
    private final String title;
    private final BooleanProperty valueProperty;
    private final VBox view = new VBox();

    public BooleanControl(String title, BooleanProperty valueProperty) {
        this.title = Objects.requireNonNull(title);
        this.valueProperty = Objects.requireNonNull(valueProperty);

        init();
    }

    private void init() {
        CheckBox checkBox = new CheckBox(title);
        checkBox.selectedProperty().bindBidirectional(valueProperty);

        view.getChildren().addAll(checkBox);
    }

    public Node asNode() {
        return view;
    }
}
