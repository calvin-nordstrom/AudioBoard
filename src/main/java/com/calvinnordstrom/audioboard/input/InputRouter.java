package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.audio.AudioCommand;
import com.calvinnordstrom.audioboard.audio.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InputRouter {
    private final Consumer<AudioCommand> commandSink;
    private final Map<InputBinding, Sound> bindings = new HashMap<>();

    public InputRouter(Consumer<AudioCommand> commandSink) {
        this.commandSink = commandSink;

        // Temporary for testing
        bindings.put(new InputBinding(Input.Source.KEYPAD, "8"), new Sound());
        bindings.put(new InputBinding(Input.Source.DESKTOP, "O"), new Sound());
        bindings.put(new InputBinding(Input.Source.DESKTOP, "P"), new Sound());
    }

    public void route(Input input) {
        InputBinding binding = new InputBinding(input.source(), input.key());
        Sound sound = bindings.get(binding);

        if (input.state() == Input.State.PRESSED) {
            if (sound != null) {
                commandSink.accept(new PlaySampleCommand(sound));
            }
        }
    }
}
