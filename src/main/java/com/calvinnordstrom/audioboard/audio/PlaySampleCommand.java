package com.calvinnordstrom.audioboard.audio;

public record PlaySampleCommand(SoundDefinition soundDefinition) implements AudioCommand {
}
