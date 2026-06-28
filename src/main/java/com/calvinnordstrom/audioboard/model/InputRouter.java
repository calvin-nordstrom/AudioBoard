package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.AudioCommand;
import com.calvinnordstrom.audioboard.audio.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.audio.StopAllSoundsCommand;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;

import java.util.List;
import java.util.function.Consumer;

public class InputRouter {
    private final List<Sound> sounds;
    private final Settings settings;
    private final Consumer<AudioCommand> commandSink;

    public InputRouter(
            List<Sound> sounds,
            Settings settings,
            Consumer<AudioCommand> commandSink
    ) {
        this.sounds = sounds;
        this.settings = settings;
        this.commandSink = commandSink;
    }

    public void route(Input input) {
        if (input.state() != Input.State.PRESSED) {
            return;
        }

        InputBinding binding = new InputBinding(input.source(), input.key());

        if (settings.getStopSoundsBinding().equals(binding)) {
            commandSink.accept(new StopAllSoundsCommand());
            return;
        }

        for (Sound sound : sounds) {
            if (!sound.isEnabled()) {
                continue;
            }

            if (!sound.getInputBinding().equals(binding)) {
                continue;
            }

            commandSink.accept(new PlaySampleCommand(sound));

            if (settings.isLocalPlaybackEnabled()) {
                System.out.println("TODO: Play sound locally: " + sound.getName());
            }
        }
    }
}
