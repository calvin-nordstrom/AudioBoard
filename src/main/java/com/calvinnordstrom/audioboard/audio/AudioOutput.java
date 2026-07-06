package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;

public interface AudioOutput extends AutoCloseable {
    AudioFormat format();

    void open();

    void start();

    void drain();

    void stop();

    @Override
    void close();

    int write(byte[] buffer, int offset, int length);
}
