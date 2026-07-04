package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.Objects;

public class ChoiceControl<T> {
    private final String title;
    private final ObjectProperty<T> valueProperty;
    private final ObservableList<T> choices;
    private final StringConverter<T> converter;
    private final VBox view = new VBox();

    public ChoiceControl(
            String title,
            ObjectProperty<T> valueProperty,
            ObservableList<T> choices
    ) {
        this(title, valueProperty, choices, null);
    }

    public ChoiceControl(
            String title,
            ObjectProperty<T> valueProperty,
            ObservableList<T> choices,
            StringConverter<T> converter
    ) {
        this.title = Objects.requireNonNull(title);
        this.valueProperty = Objects.requireNonNull(valueProperty);
        this.choices = Objects.requireNonNull(choices);
        this.converter = converter;

        init();
    }

    private void init() {
        Label titleLabel = new Label(title);

        ComboBox<T> comboBox = new ComboBox<>(choices);
        comboBox.valueProperty().bindBidirectional(valueProperty);

        if (converter != null) {
            comboBox.setConverter(converter);
        }

        view.getChildren().addAll(titleLabel, comboBox);
    }

    public Node asNode() {
        return view;
    }
}
