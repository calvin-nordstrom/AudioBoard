package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public final class SoundLoader {
    public Sound load(File file) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {
            AudioFormat format = ais.getFormat();

            validate(format);

            byte[] pcm16 = ais.readAllBytes();
            float[] pcm = decode16BitPcm(pcm16, format);

            return new Sound(
                    pcm,
                    pcm16,
                    (int) format.getSampleRate(),
                    format.getChannels()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(AudioFormat format) {
        if (format.getSampleSizeInBits() != 16) {
            throw new IllegalArgumentException("Only 16-bit PCM audio is supported");
        }

        if (format.isBigEndian()) {
            throw new IllegalArgumentException("Only little-endian PCM is supported");
        }
    }

    private float[] decode16BitPcm(byte[] raw, AudioFormat format) {
        float[] pcm = new float[raw.length / 2];

        for (int i = 0; i < raw.length; i += 2) {
            short sample = (short) (((raw[i + 1] & 0xFF) << 8) | (raw[i] & 0xFF));
            pcm[i / 2] = sample / 32768f;
        }

        return pcm;
    }
}