package com.calvinnordstrom.audioboard.audio.command;

import com.calvinnordstrom.audioboard.audio.AbstractAudioMixer;
import com.calvinnordstrom.audioboard.audio.AudioInput;

public record ChangeInputCommand(AudioInput input) implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.setInput(input);
    }
}
