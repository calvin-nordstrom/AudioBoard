package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class LocalAudioMixer extends AbstractAudioMixer {
    private static final String MIXER_THREAD_NAME = "AudioBoard Local Mixer Thread";
    private static final int BUFFER_SIZE = 4096;
    private final SourceDataLine outputLine;

    public LocalAudioMixer(SourceDataLine outputLine) {
        this.outputLine = outputLine;
    }

    @Override
    public synchronized void start() {
        if (running) {
            return;
        }

        validateFormats();

        try {
            outputLine.open(outputLine.getFormat(), BUFFER_SIZE);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        running = true;

        mixerThread = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(MIXER_THREAD_NAME);
            return thread;
        });
        mixerThread.submit(this::mixerLoop);

        outputLine.start();
    }

    @Override
    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;

        if (mixerThread != null) {
            mixerThread.shutdownNow();
        }

        stopAllSounds();

        outputLine.drain();
        outputLine.stop();
        outputLine.close();
    }

    private void mixerLoop() {
        AudioFormat format = outputLine.getFormat();
        boolean bigEndian = format.isBigEndian();
        int samplesPerChunk = BUFFER_SIZE / 2;
        float[] mixSamples = new float[samplesPerChunk];
        byte[] outputBytes = new byte[BUFFER_SIZE];

        try {
            while (running) {
                Arrays.fill(mixSamples, 0f);

                // Active sounds

                for (ActiveSound active : activeSounds) {
                    if (active.finished()) {
                        activeSounds.remove(active);
                        continue;
                    }

                    float[] pcm = active.soundAsset.pcm();
                    int remaining = pcm.length - active.position;

                    int count = Math.min(samplesPerChunk, remaining);
                    for (int i = 0; i < count; i++) {
                        mixSamples[i] += pcm[active.position++] * active.volume;
                    }

                    if (active.finished()) {
                        activeSounds.remove(active);
                    }
                }

                // Clipping

                for (int i = 0; i < samplesPerChunk; i++) {
                    float sample = mixSamples[i];

                    if (sample > 1f) {
                        sample = 1f;
                    } else if (sample < -1f) {
                        sample = -1f;
                    }

                    mixSamples[i] = sample;
                }

                encodePcm16(mixSamples, samplesPerChunk, outputBytes, bigEndian);

                outputLine.write(outputBytes, 0, samplesPerChunk * 2);
            }
        } finally {
            running = false;
        }
    }

    private void validateFormats() {
        AudioFormat out = outputLine.getFormat();

        if (out.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new IllegalStateException("Only PCM_SIGNED is supported");
        }

        if (out.getSampleSizeInBits() != 16) {
            throw new IllegalStateException("Only 16-bit PCM is supported");
        }
    }
}
