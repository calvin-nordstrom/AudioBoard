package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.FloatProperty;

import java.util.function.DoubleUnaryOperator;

public class FloatRangeControl extends NumberRangeControl<Float> {
    public FloatRangeControl(
            String title,
            FloatProperty valueProperty,
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
                displayToModel
        );
    }

    @Override
    protected Float fromDouble(double value) {
        return (float) value;
    }
}
