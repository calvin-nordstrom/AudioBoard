package com.calvinnordstrom.audioboard.audio.command;

import com.calvinnordstrom.audioboard.audio.AbstractAudioMixer;
import com.calvinnordstrom.audioboard.audio.Sound;

public record PlaySampleCommand(Sound sound) implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.addSound(sound.getSoundAsset(), sound.getVolume());
    }
}
