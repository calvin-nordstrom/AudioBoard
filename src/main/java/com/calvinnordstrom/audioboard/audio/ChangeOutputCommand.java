package com.calvinnordstrom.audioboard.audio;

public record ChangeOutputCommand(AudioOutput output) implements AudioCommand {

    @Override
    public void execute(AbstractAudioMixer mixer) {
        mixer.setOutput(output);
    }
}
