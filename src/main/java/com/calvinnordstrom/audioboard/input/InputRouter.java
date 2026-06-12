package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.audio.AudioCommand;
import com.calvinnordstrom.audioboard.audio.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.SoundDefinition;

import java.util.List;
import java.util.function.Consumer;

public class InputRouter {
    private final Consumer<AudioCommand> commandSink;
    private final List<SoundDefinition> sounds;

    public InputRouter(List<SoundDefinition> sounds, Consumer<AudioCommand> commandSink) {
        this.sounds = sounds;
        this.commandSink = commandSink;
    }

    public void route(Input input) {
        if (input.state() != Input.State.PRESSED) {
            return;
        }

        InputBinding binding = new InputBinding(input.source(), input.key());

        for (SoundDefinition sound : sounds) {
            if (!sound.getInputBinding().equals(binding)) {
                continue;
            }

            if (!sound.isEnabled()) {
                continue;
            }

            commandSink.accept(new PlaySampleCommand(sound));
        }
    }
}
