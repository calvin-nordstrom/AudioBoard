package com.calvinnordstrom.audioboard.audio.command;

import com.calvinnordstrom.audioboard.audio.*;

public sealed interface AudioCommand permits
        PlaySampleCommand,
        StopSampleCommand,
        StopAllSoundsCommand,
        ChangeInputCommand,
        ChangeOutputCommand {
    void execute(AbstractAudioMixer mixer);
}
