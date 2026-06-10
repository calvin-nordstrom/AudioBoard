package com.calvinnordstrom.audioboard.audio;

public final class SoundAsset {
    private final float[] pcm;
    private final byte[] pcm16;
    private final int sampleRate;
    private final int channels;
    private final int frameCount;

    public SoundAsset(float[] pcm, byte[] pcm16, int sampleRate, int channels) {
        this.pcm = pcm;
        this.pcm16 = pcm16;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.frameCount = pcm.length / channels;
    }

    public float[] getPcm() {
        return pcm;
    }

    public byte[] getPcm16() {
        return pcm16;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public double getDurationSeconds() {
        return (double) frameCount / sampleRate;
    }
}
