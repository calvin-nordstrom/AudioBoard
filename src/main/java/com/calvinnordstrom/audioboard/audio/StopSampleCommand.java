package com.calvinnordstrom.audioboard.audio;

public record StopSampleCommand(Sound sound) implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.stopSound(sound.getSoundAsset());
    }
}
