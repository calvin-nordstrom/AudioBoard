package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class JavaSoundOutput implements AudioOutput {
    private final SourceDataLine line;
    private final int bufferSize;

    public JavaSoundOutput(SourceDataLine line, int bufferSize) {
        this.line = line;
        this.bufferSize = bufferSize;
    }

    @Override
    public AudioFormat format() {
        return line.getFormat();
    }

    @Override
    public void open() {
        try {
            line.open(line.getFormat(), bufferSize);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        line.start();
    }

    @Override
    public void drain() {
        line.drain();
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
    public int write(byte[] buffer, int offset, int length) {
        return line.write(buffer, offset, length);
    }
}
