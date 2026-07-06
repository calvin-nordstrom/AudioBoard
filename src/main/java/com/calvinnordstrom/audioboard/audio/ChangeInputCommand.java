package com.calvinnordstrom.audioboard.audio;

public record ChangeInputCommand(AudioInput input) implements AudioCommand {
    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.setInput(input);
    }
}
