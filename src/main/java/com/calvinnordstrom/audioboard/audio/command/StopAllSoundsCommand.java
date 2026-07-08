package com.calvinnordstrom.audioboard.audio.command;

import com.calvinnordstrom.audioboard.audio.AbstractAudioMixer;

public record StopAllSoundsCommand() implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.stopAllSounds();
    }
}
