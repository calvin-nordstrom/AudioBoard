package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

public abstract class AbstractNumberControl<T extends Number> {
    private final VBox view = new VBox();

    protected AbstractNumberControl(
            String title,
            Property<T> valueProperty,
            double displayMin,
            double displayMax,
            String labelFormat,
            DoubleUnaryOperator modelToDisplay,
            DoubleUnaryOperator displayToModel
    ) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(valueProperty);
        Objects.requireNonNull(labelFormat);
        Objects.requireNonNull(modelToDisplay);
        Objects.requireNonNull(displayToModel);

        Label titleLabel = new Label(title);

        double initialDisplayValue =
                modelToDisplay.applyAsDouble(valueProperty.getValue().doubleValue());

        Slider slider = new Slider(displayMin, displayMax, initialDisplayValue);

        Label valueLabel = new Label(String.format(labelFormat, initialDisplayValue));

        slider.valueProperty().addListener((_, _, newValue) -> {
            double modelValue = displayToModel.applyAsDouble(newValue.doubleValue());
            valueProperty.setValue(fromDouble(modelValue));
        });

        valueProperty.addListener((_, _, newValue) -> {
            double displayValue = modelToDisplay.applyAsDouble(newValue.doubleValue());

            if (slider.getValue() != displayValue) {
                slider.setValue(displayValue);
            }

            valueLabel.setText(String.format(labelFormat, displayValue));
        });

        HBox sliderPane = new HBox(slider, valueLabel);

        view.getChildren().addAll(titleLabel, sliderPane);
    }

    protected abstract T fromDouble(double value);

    public Node asNode() {
        return view;
    }
}
