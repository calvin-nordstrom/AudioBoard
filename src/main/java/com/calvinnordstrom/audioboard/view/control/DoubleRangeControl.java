package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.DoubleProperty;

import java.util.function.DoubleUnaryOperator;

public class DoubleRangeControl extends NumberRangeControl<Double> {
    public DoubleRangeControl(
            String title,
            DoubleProperty valueProperty,
            double displayMin,
            double displayMax,
            String labelFormat,
            DoubleUnaryOperator modelToDisplay,
            DoubleUnaryOperator displayToModel
    ) {
        super(
                title,
                valueProperty.asObject(),
                displayMin,
                displayMax,
                labelFormat,
                modelToDisplay,
                displayToModel);
    }

    @Override
    protected Double fromDouble(double value) {
        return value;
    }
}
