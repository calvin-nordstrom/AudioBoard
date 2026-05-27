package com.calvinnordstrom.audioboard.audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioEngine {
    private final BlockingQueue<AudioCommand> commandQueue = new LinkedBlockingQueue<>();
    private final ExecutorService commandThread = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("AudioBoard Audio Command Thread");
        return thread;
    });

    public void start() {
        commandThread.submit(this::processLoop);
    }

    public void stop() {
        commandThread.shutdownNow();
    }

    public void submit(AudioCommand command) {
        commandQueue.offer(command);
    }

    private void processLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                AudioCommand command = commandQueue.take();
                handleCommand(command);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleCommand(AudioCommand command) {
        if (command instanceof PlaySampleCommand play) {
            playSample();
        }
    }

    private void playSample() {
        System.out.println(Thread.currentThread().getName() + ": Playing sample");

        // future:
        // allocate voice
        // schedule playback
        // mix into output
    }
}
