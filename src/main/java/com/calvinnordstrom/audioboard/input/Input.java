package com.calvinnordstrom.audioboard.input;

public record Input(Source source, State state, String key) {
    public enum Source {
        KEYPAD,
        DESKTOP
    }

    public enum State {
        PRESSED,
        RELEASED
    }
}
