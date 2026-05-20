package com.calvinnordstrom.audioboard.input;

public class Input {
    public enum Source {
        KEYPAD,
        DESKTOP
    }
    public enum State {
        PRESSED,
        RELEASED
    }
    private final Source source;
    private final State state;
    private final String key;

    public Input(Source source, State state, String key) {
        this.source = source;
        this.state = state;
        this.key = key;
    }

    public Source getType() {
        return source;
    }

    public State getState() {
        return state;
    }

    public String getKey() {
        return key;
    }
}
