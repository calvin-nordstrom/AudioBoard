package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.audio.AudioCommand;
import com.calvinnordstrom.audioboard.audio.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.Sound;

import java.util.List;
import java.util.function.Consumer;

public class InputRouter {
    private final Consumer<AudioCommand> commandSink;
    private final List<Sound> sounds;

    public InputRouter(List<Sound> sounds, Consumer<AudioCommand> commandSink) {
        this.sounds = sounds;
        this.commandSink = commandSink;
    }

    public void route(Input input) {
        if (input.state() != Input.State.PRESSED) {
            return;
        }

        InputBinding binding = new InputBinding(input.source(), input.key());

        for (Sound sound : sounds) {
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
