package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class AudioMixer extends AbstractAudioMixer {
    private static final String MIXER_THREAD_NAME = "AudioBoard Audio Mixer Thread";
    private static final int BUFFER_SIZE = 4096;
    private AudioInput input;
    private AudioOutput output;

    public AudioMixer(AudioInput input, AudioOutput output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public synchronized void start() {
        if (running) {
            return;
        }

        validateFormats(input.format(), output.format());

        input.open();
        output.open();

        running = true;

        mixerThread = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(MIXER_THREAD_NAME);
            return thread;
        });
        mixerThread.submit(this::mixerLoop);

        input.start();
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

        input.stop();
        input.close();

        output.drain();
        output.stop();
        output.close();
    }

    private void mixerLoop() {
        AudioFormat format = output.format();
        boolean bigEndian = format.isBigEndian();
        byte[] micBytes = new byte[BUFFER_SIZE];
        int samplesPerChunk = BUFFER_SIZE / 2;
        float[] micSamples = new float[samplesPerChunk];
        float[] mixSamples = new float[samplesPerChunk];
        byte[] outputBytes = new byte[BUFFER_SIZE];

        try {
            while (running) {
                AudioInput currentInput = input;
                AudioOutput currentOutput = output;

                Arrays.fill(mixSamples, 0f);

                // Microphone

                int bytesRead = currentInput.read(micBytes, 0, micBytes.length);

                decodePcm16(micBytes, bytesRead, micSamples, bigEndian);

                int micSampleCount = bytesRead / 2;
                for (int i = 0; i < micSampleCount; i++) {
                    mixSamples[i] += micSamples[i];
                }

                // Active sounds

                for (ActiveSound active : activeSounds) {
                    if (active.isFinished()) {
                        activeSounds.remove(active);
                        continue;
                    }

                    float[] pcm = active.getSoundAsset().pcm();
                    int remaining = pcm.length - active.getPosition();

                    int count = Math.min(micSampleCount, remaining);
                    for (int i = 0; i < count; i++) {
                        mixSamples[i] += pcm[active.getPositionAndIncrement()] * active.getVolume();
                    }

                    if (active.isFinished()) {
                        activeSounds.remove(active);
                    }
                }

                // Clipping

                for (int i = 0; i < micSampleCount; i++) {
                    float sample = mixSamples[i];

                    if (sample > 1f) {
                        sample = 1f;
                    } else if (sample < -1f) {
                        sample = -1f;
                    }

                    mixSamples[i] = sample;
                }

                encodePcm16(mixSamples, micSampleCount, outputBytes, bigEndian);

                currentOutput.write(outputBytes, 0, micSampleCount * 2);
            }
        } finally {
            running = false;
        }
    }

    public synchronized void setInput(AudioInput newInput) {
        newInput.open();
        newInput.start();

        AudioInput old = input;
        input = newInput;

        old.stop();
        old.close();
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

    private void validateFormats(AudioFormat in, AudioFormat out) {
        if (in.getEncoding() != AudioFormat.Encoding.PCM_SIGNED
                || out.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new IllegalStateException("Only PCM_SIGNED is supported");
        }

        if (in.getSampleRate() != out.getSampleRate()) {
            throw new IllegalStateException("Sample rate mismatch");
        }

        if (in.getSampleSizeInBits() != 16
                || out.getSampleSizeInBits() != 16) {
            throw new IllegalStateException("Only 16-bit PCM is supported");
        }

        if (in.getChannels() != out.getChannels()) {
            throw new IllegalStateException("Channel count mismatch");
        }

        if (in.isBigEndian() != out.isBigEndian()) {
            throw new IllegalStateException("Endian mismatch");
        }
    }
}
