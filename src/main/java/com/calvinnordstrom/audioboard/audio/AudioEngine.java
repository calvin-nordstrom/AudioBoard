package com.calvinnordstrom.audioboard.audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioEngine {
    private static final String COMMAND_THREAD_NAME = "AudioBoard Audio Command Thread";
    private final AbstractAudioMixer mixer;
    private final BlockingQueue<AudioCommand> commandQueue = new LinkedBlockingQueue<>();
    private ExecutorService commandThread;
    private volatile boolean running;

    public AudioEngine(AbstractAudioMixer mixer) {
        this.mixer = mixer;
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

                if (cmd instanceof PlaySampleCommand(Sound sound)) {
                    SoundAsset soundAsset = sound.getSoundAsset();
                    float volume = sound.getVolume();

                    mixer.addSound(soundAsset, volume);
                } else if (cmd instanceof StopSampleCommand(Sound sound)) {
                    mixer.stopSound(sound.getSoundAsset());
                } else if (cmd instanceof StopAllSoundsCommand) {
                    mixer.stopAllSounds();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
