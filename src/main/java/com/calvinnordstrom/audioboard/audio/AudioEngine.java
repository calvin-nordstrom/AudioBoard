package com.calvinnordstrom.audioboard.audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioEngine {
    private static final String COMMAND_THREAD_NAME = "AudioBoard Audio Command Thread";
    private final BlockingQueue<AudioCommand> commandQueue = new LinkedBlockingQueue<>();
    private ExecutorService commandThread;
    private volatile boolean running;
    private final AudioMixer mixer;

    public AudioEngine(TargetDataLine mic, SourceDataLine out) {
        mixer = new AudioMixer(mic, out);
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;

        commandThread = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(COMMAND_THREAD_NAME);
            return thread;
        });
        commandThread.submit(this::commandLoop);

        mixer.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;

        if (commandThread != null) {
            commandThread.shutdownNow();
        }

        mixer.stop();
    }

    public void submit(AudioCommand command) {
        commandQueue.offer(command);
    }

    private void commandLoop() {
        while (running) {
            try {
                AudioCommand cmd = commandQueue.take();
                if (cmd instanceof PlaySampleCommand(SoundDefinition soundDefinition)) {
                    SoundAsset soundAsset = soundDefinition.getSoundAsset();
                    float volume = soundDefinition.getVolume();
                    mixer.addSound(soundAsset, volume);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
