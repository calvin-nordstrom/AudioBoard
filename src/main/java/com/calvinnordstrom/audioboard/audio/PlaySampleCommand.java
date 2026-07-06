package com.calvinnordstrom.audioboard.audio;

public record PlaySampleCommand(Sound sound) implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.addSound(sound.getSoundAsset(), sound.getVolume());
    }
}
