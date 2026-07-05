package com.calvinnordstrom.audioboard.model;

public record Change<T>(
        Object source,
        String property,
        T oldValue,
        T newValue
) {
}
