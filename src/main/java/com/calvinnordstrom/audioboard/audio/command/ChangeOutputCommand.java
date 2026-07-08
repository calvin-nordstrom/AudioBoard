package com.calvinnordstrom.audioboard.audio.command;

import com.calvinnordstrom.audioboard.audio.AbstractAudioMixer;
import com.calvinnordstrom.audioboard.audio.AudioOutput;

public record ChangeOutputCommand(AudioOutput output) implements AudioCommand {

    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.setOutput(output);
    }
}
