package com.calvinnordstrom.audioboard.input;

import java.io.Serializable;

public record Input(Source source, State state, String key) implements Serializable {
    public enum Source {
        KEYPAD,
        DESKTOP
    }

    public enum State {
        PRESSED,
        RELEASED
    }
}
