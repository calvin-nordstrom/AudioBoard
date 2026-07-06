package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class JavaSoundInput implements AudioInput {
    private final TargetDataLine line;

    public JavaSoundInput(TargetDataLine line) {
        this.line = line;
    }

    @Override
    public AudioFormat format() {
        return line.getFormat();
    }

    @Override
    public void open() {
        try {
            line.open();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        line.start();
    }

    @Override
    public void stop() {
        line.stop();
    }

    @Override
    public void close() {
        line.close();
    }

    @Override
    public int read(byte[] buffer, int offset, int length) {
        return line.read(buffer, offset, length);
    }
}
