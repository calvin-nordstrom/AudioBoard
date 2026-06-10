package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.audio.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InputRouter {
    private final Consumer<AudioCommand> commandSink;
    private final List<SoundDefinition> sounds = new ArrayList<>();

    public InputRouter(Consumer<AudioCommand> commandSink) {
        this.commandSink = commandSink;

        // Temporarily hardcoded; fill in your own file paths
        sounds.add(new SoundDefinition("Test1",
                Paths.get(""),
                Paths.get(""),
                new InputBinding(Input.Source.DESKTOP, "O"),
                0.8f,
                true
        ));
        sounds.add(new SoundDefinition("Test2",
                Paths.get(""),
                Paths.get(""),
                new InputBinding(Input.Source.DESKTOP, "P"),
                0.8f,
                true
        ));
    }

    public void route(Input input) {
        if (input.state() != Input.State.PRESSED) {
            return;
        }

        InputBinding binding = new InputBinding(input.source(), input.key());

        for (SoundDefinition soundDefinition : sounds) {
            if (!soundDefinition.getInputBinding().equals(binding)) {
                continue;
            }

            if (!soundDefinition.isEnabled()) {
                continue;
            }

            commandSink.accept(new PlaySampleCommand(soundDefinition));
        }
    }
}
