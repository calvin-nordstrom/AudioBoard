package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class AudioUtils {
    public static final AudioFormat DEFAULT_FORMAT = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100, // Sample rate (Hz)
            16,    // Sample size (bits)
            2,     // Channels (stereo)
            4,     // Frame size (bytes)
            44100, // Frame rate (Hz)
            false  // Little-endian
    );
    private static final DataLine.Info TARGET_DATA_LINE_INFO = new DataLine.Info(TargetDataLine.class, DEFAULT_FORMAT);
    private static final DataLine.Info SOURCE_DATA_LINE_INFO = new DataLine.Info(SourceDataLine.class, DEFAULT_FORMAT);

    public static TargetDataLine getTargetByName(String name) {
        try {
            Mixer.Info mixerInfo = getMixerInfoByName(name);

            if (mixerInfo == null) {
                return null;
            }

            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            return (TargetDataLine) mixer.getLine(TARGET_DATA_LINE_INFO);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static SourceDataLine getSourceByName(String name) {
        try {
            Mixer.Info mixerInfo = getMixerInfoByName(name);

            if (mixerInfo == null) {
                return null;
            }

            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            return (SourceDataLine) mixer.getLine(SOURCE_DATA_LINE_INFO);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static Mixer.Info getMixerInfoByName(String name) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            if (info.getName().contains(name)) {
                return info;
            }
        }
        return null;
    }

    public static List<Mixer.Info> getInputDevices() {
        List<Mixer.Info> devices = new ArrayList<>();

        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);

            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {
                if (TargetDataLine.class.isAssignableFrom(lineInfo.getLineClass())) {
                    devices.add(info);
                    break;
                }
            }
        }

        return devices;
    }

    public static List<Mixer.Info> getOutputDevices() {
        List<Mixer.Info> devices = new ArrayList<>();

        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);

            for (Line.Info lineInfo : mixer.getSourceLineInfo()) {
                if (SourceDataLine.class.isAssignableFrom(lineInfo.getLineClass())) {
                    devices.add(info);
                    break;
                }
            }
        }

        return devices;
    }
}
