package com.calvinnordstrom.audioboard.audio;

public record StopSampleCommand(Sound sound) implements AudioCommand {
}
