package com.calvinnordstrom.audioboard.view.control;

import javafx.beans.property.LongProperty;

import java.util.function.DoubleUnaryOperator;

public class LongRangeControl extends NumberRangeControl<Long> {
    public LongRangeControl(
            String title,
            LongProperty valueProperty,
            long min,
            long max
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
    protected Long fromDouble(double value) {
        return Math.round(value);
    }
}
