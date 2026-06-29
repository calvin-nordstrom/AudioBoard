package com.calvinnordstrom.audioboard.audio;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public abstract class AbstractAudioMixer {
    protected final ConcurrentLinkedQueue<ActiveSound> activeSounds = new ConcurrentLinkedQueue<>();
    protected ExecutorService mixerThread;
    protected volatile boolean running;

    public abstract void start();

    public abstract void stop();

    public PlayingSound addSound(SoundAsset soundAsset, float volume) {
        if (soundAsset == null || soundAsset.pcm().length == 0) {
            return null;
        }

        float clampedVolume = Math.clamp(volume, 0f, 1f);
        ActiveSound activeSound = new ActiveSound(soundAsset, clampedVolume);

        activeSounds.add(activeSound);

        return new PlayingSound(activeSound);
    }

    public void stopAllSounds() {
        for (ActiveSound activeSound : activeSounds) {
            activeSound.stop();
        }

        activeSounds.clear();
    }

    protected void decodePcm16(
            byte[] bytes,
            int bytesRead,
            float[] samples,
            boolean bigEndian
    ) {
        int sampleCount = bytesRead / 2;
        for (int i = 0; i < sampleCount; i++) {
            int byteIndex = i * 2;

            short s;
            if (bigEndian) {
                s = (short) (((bytes[byteIndex] & 0xff) << 8) | (bytes[byteIndex + 1] & 0xff));
            } else {
                s = (short) ((bytes[byteIndex] & 0xff) | ((bytes[byteIndex + 1] & 0xff) << 8));
            }

            samples[i] = s / 32768f;
        }
    }

    protected void encodePcm16(
            float[] samples,
            int sampleCount,
            byte[] output,
            boolean bigEndian
    ) {
        for (int i = 0; i < sampleCount; i++) {
            short s = (short) (samples[i] * 32767f);
            int byteIndex = i * 2;

            if (bigEndian) {
                output[byteIndex] = (byte) ((s >> 8) & 0xff);
                output[byteIndex + 1] = (byte) (s & 0xff);
            } else {
                output[byteIndex] = (byte) (s & 0xff);
                output[byteIndex + 1] = (byte) ((s >> 8) & 0xff);
            }
        }
    }
}
