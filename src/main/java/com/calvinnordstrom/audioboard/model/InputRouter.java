package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.audio.AudioEngine;
import com.calvinnordstrom.audioboard.audio.command.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.Sound;
import com.calvinnordstrom.audioboard.audio.command.StopAllSoundsCommand;
import com.calvinnordstrom.audioboard.input.Input;
import com.calvinnordstrom.audioboard.input.InputBinding;

import java.util.List;

public class InputRouter {
    private final List<Sound> sounds;
    private final Settings settings;
    private final AudioEngine virtualEngine;
    private final AudioEngine localEngine;

    public InputRouter(
            List<Sound> sounds,
            Settings settings,
            AudioEngine virtualEngine,
            AudioEngine localEngine
    ) {
        this.sounds = sounds;
        this.settings = settings;
        this.virtualEngine = virtualEngine;
        this.localEngine = localEngine;
    }

    public void route(Input input) {
        if (input.state() != Input.State.PRESSED) {
            return;
        }

        InputBinding binding = new InputBinding(input.source(), input.key());

        if (binding.equals(settings.getStopSoundsBinding())) {
            virtualEngine.submit(new StopAllSoundsCommand());
            localEngine.submit(new StopAllSoundsCommand());
            return;
        }

        for (Sound sound : sounds) {
            if (!sound.isEnabled()) {
                continue;
            }

            if (!binding.equals(sound.getInputBinding())) {
                continue;
            }

            virtualEngine.submit(new PlaySampleCommand(sound));

            if (settings.isLocalPlaybackEnabled()) {
                localEngine.submit(new PlaySampleCommand(sound));
            }
        }
    }
}
