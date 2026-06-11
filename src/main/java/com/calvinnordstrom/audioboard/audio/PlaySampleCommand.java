package com.calvinnordstrom.audioboard.audio;

public record PlaySampleCommand(SoundDefinition sound) implements AudioCommand {
}
