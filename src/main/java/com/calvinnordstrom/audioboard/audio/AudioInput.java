package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;

public interface AudioInput extends AutoCloseable {
    AudioFormat format();

    void open();

    void start();

    void stop();

    @Override
    void close();

    int read(byte[] buffer, int offset, int length);
}
