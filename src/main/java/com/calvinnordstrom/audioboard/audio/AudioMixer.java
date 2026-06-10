package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioMixer {
    private static final String MIXER_THREAD_NAME = "AudioBoard Audio Mixer Thread";
    private static final int BUFFER_SIZE = 512;
    private final TargetDataLine inputLine;
    private final SourceDataLine outputLine;
    private ExecutorService mixerThread;
    private final ConcurrentLinkedQueue<ActiveSound> activeSounds = new ConcurrentLinkedQueue<>();
    private volatile boolean running;

    public AudioMixer(TargetDataLine inputLine, SourceDataLine outputLine) {
        this.inputLine = inputLine;
        this.outputLine = outputLine;
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        validateFormats();

        try {
            inputLine.open();
            outputLine.open();
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

        inputLine.start();
        outputLine.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;

        if (mixerThread != null) {
            mixerThread.shutdownNow();
        }

        stopAllSounds();

        inputLine.stop();
        inputLine.close();

        outputLine.drain();
        outputLine.stop();
        outputLine.close();
    }

    public PlayingSound addSound(Sound sound, float volume) {
        if (sound == null || sound.getPcm().length == 0) {
            return null;
        }

        float clampedVolume = Math.clamp(volume, 0f, 1f);
        ActiveSound activeSound = new ActiveSound(sound, clampedVolume);

        activeSounds.add(activeSound);

        return new PlayingSound(activeSound);
    }

    public void stopAllSounds() {
        for (ActiveSound activeSound : activeSounds) {
            activeSound.stop();
        }

        activeSounds.clear();
    }

    private void mixerLoop() {
        AudioFormat format = outputLine.getFormat();
        boolean bigEndian = format.isBigEndian();
        byte[] micBytes = new byte[BUFFER_SIZE];
        int samplesPerChunk = BUFFER_SIZE / 2;
        float[] micSamples = new float[samplesPerChunk];
        float[] mixSamples = new float[samplesPerChunk];
        byte[] outputBytes = new byte[BUFFER_SIZE];

        try {
            while (running) {
                Arrays.fill(mixSamples, 0f);

                // Microphone

                int bytesRead = inputLine.read(micBytes, 0, micBytes.length);

                decodePcm16(micBytes, bytesRead, micSamples, bigEndian);

                int micSampleCount = bytesRead / 2;
                for (int i = 0; i < micSampleCount; i++) {
                    mixSamples[i] += micSamples[i];
                }

                // Active sounds

                for (ActiveSound active : activeSounds) {
                    if (active.finished()) {
                        activeSounds.remove(active);
                        continue;
                    }

                    float[] pcm = active.sound.getPcm();
                    int remaining = pcm.length - active.position;

                    int count = Math.min(micSampleCount, remaining);
                    for (int i = 0; i < count; i++) {
                        mixSamples[i] += pcm[active.position++] * active.volume;
                    }

                    if (active.finished()) {
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

                outputLine.write(outputBytes, 0, micSampleCount * 2);
            }
        } finally {
            running = false;
        }
    }

    private void validateFormats() {
        AudioFormat in = inputLine.getFormat();
        AudioFormat out = outputLine.getFormat();

        if (in.getEncoding() != AudioFormat.Encoding.PCM_SIGNED
                || out.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            throw new IllegalStateException("Only PCM_SIGNED is supported");
        }

        if (in.getSampleRate() != out.getSampleRate()) {
            throw new IllegalStateException("Sample rate mismatch");
        }

        if (in.getSampleSizeInBits() != 16 || out.getSampleSizeInBits() != 16) {
            throw new IllegalStateException("Only 16-bit PCM is supported");
        }

        if (in.getChannels() != out.getChannels()) {
            throw new IllegalStateException("Channel count mismatch");
        }

        if (in.getFrameSize() != out.getFrameSize()) {
            throw new IllegalStateException("Frame size mismatch");
        }

        if (in.getFrameRate() != out.getFrameRate()) {
            throw new IllegalStateException("Frame rate mismatch");
        }

        if (in.isBigEndian() != out.isBigEndian()) {
            throw new IllegalStateException("Endian mismatch");
        }
    }

    private static void decodePcm16(byte[] bytes,
                                    int bytesRead,
                                    float[] samples,
                                    boolean bigEndian) {
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

    private static void encodePcm16(float[] samples,
                                    int sampleCount,
                                    byte[] output,
                                    boolean bigEndian) {
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
