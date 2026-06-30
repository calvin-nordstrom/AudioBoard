package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.IntegerProperty;

import java.util.function.DoubleUnaryOperator;

public class IntegerRangeControl extends NumberRangeControl<Integer> {
    public IntegerRangeControl(
            String title,
            IntegerProperty valueProperty,
            int min,
            int max
    ) {
        super(
                title,
                valueProperty.asObject(),
                min,
                max,
                "%.0f",
                DoubleUnaryOperator.identity(),
                DoubleUnaryOperator.identity()
        );
    }

    @Override
    protected Integer fromDouble(double value) {
        return (int) Math.round(value);
    }
}
