package com.calvinnordstrom.audioboard.audio.command;

import com.calvinnordstrom.audioboard.audio.AbstractAudioMixer;
import com.calvinnordstrom.audioboard.audio.Sound;

public record StopSampleCommand(Sound sound) implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.stopSound(sound.getSoundAsset());
    }
}
