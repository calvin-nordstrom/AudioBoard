package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class LocalAudioMixer extends AbstractAudioMixer {
    private static final String MIXER_THREAD_NAME = "AudioBoard Local Mixer Thread";
    private static final int BUFFER_SIZE = 4096;
    private AudioOutput output;

    public LocalAudioMixer(AudioOutput output) {
        this.output  = output;
    }

    @Override
    public synchronized void start() {
        if (running) {
            return;
        }

        validateFormat(output.format());

        output.open();

        running = true;

        mixerThread = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(MIXER_THREAD_NAME);
            return thread;
        });
        mixerThread.submit(this::mixerLoop);

        output.start();
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

        output.drain();
        output.stop();
        output.close();
    }

    private void mixerLoop() {
        AudioFormat format = output.format();
        boolean bigEndian = format.isBigEndian();
        int samplesPerChunk = BUFFER_SIZE / 2;
        float[] mixSamples = new float[samplesPerChunk];
        byte[] outputBytes = new byte[BUFFER_SIZE];

        try {
            while (running) {
                AudioOutput currentOutput = output;

                Arrays.fill(mixSamples, 0f);

                // Active sounds

                for (ActiveSound active : activeSounds) {
                    if (active.isFinished()) {
                        activeSounds.remove(active);
                        continue;
                    }

                    float[] pcm = active.getSoundAsset().pcm();
                    int remaining = pcm.length - active.getPosition();

                    int count = Math.min(samplesPerChunk, remaining);
                    for (int i = 0; i < count; i++) {
                        mixSamples[i] += pcm[active.getPositionAndIncrement()] * active.getVolume();
                    }

                    if (active.isFinished()) {
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

                currentOutput.write(outputBytes, 0, samplesPerChunk * 2);
            }
        } finally {
            running = false;
        }
    }

    public synchronized void setOutput(AudioOutput newOutput) {
        newOutput.open();
        newOutput.start();

        AudioOutput old = output;
        output = newOutput;

        old.drain();
        old.stop();
        old.close();
    }

    private void validateFormat(AudioFormat out) {
        if (out.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new IllegalStateException("Only PCM_SIGNED is supported");
        }

        if (out.getSampleSizeInBits() != 16) {
            throw new IllegalStateException("Only 16-bit PCM is supported");
        }
    }
}
