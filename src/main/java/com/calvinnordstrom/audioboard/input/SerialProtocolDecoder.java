package com.calvinnordstrom.audioboard.input;

public class SerialProtocolDecoder {
    public Input decode(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }

        char sourceChar = line.charAt(0);
        char stateChar = line.charAt(1);
        String key = line.substring(2);

        Input.Source source = switch (sourceChar) {
            case 'K' -> Input.Source.KEYPAD;
            case 'D' -> Input.Source.DESKTOP;
            default -> null;
        };

        Input.State state = switch (stateChar) {
            case 'P' -> Input.State.PRESSED;
            case 'R' -> Input.State.RELEASED;
            default -> null;
        };

        if (source == null || state == null || key.isEmpty()) {
            return null;
        }

        return new Input(source, state, key);
    }
}
