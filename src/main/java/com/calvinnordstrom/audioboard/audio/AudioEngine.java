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
    private final AudioMixer mixer;

    public AudioEngine(TargetDataLine mic, SourceDataLine out) {
        mixer = new AudioMixer(mic, out);
    }

    public void start() {
        mixer.start();

        commandThread = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(COMMAND_THREAD_NAME);
            return thread;
        });
        commandThread.submit(this::commandLoop);
    }

    public void stop() {
        mixer.stop();

        if (commandThread != null) {
            commandThread.shutdownNow();
        }
    }

    public void submit(AudioCommand command) {
        commandQueue.offer(command);
    }

    private void commandLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                AudioCommand cmd = commandQueue.take();
                if (cmd instanceof PlaySampleCommand p) {
                    mixer.addSound(p.sound(), 1.0f);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
