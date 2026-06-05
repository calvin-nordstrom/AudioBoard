package com.calvinnordstrom.audioboard.audio;

public record PlaySampleCommand(Sound sound) implements AudioCommand {
}
