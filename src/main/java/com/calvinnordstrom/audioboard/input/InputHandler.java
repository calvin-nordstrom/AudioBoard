package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.Main;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputHandler {
    private final KeyListener keyListener;
    private final SerialListener serialInputListener;
    private final BlockingQueue<Input> inputQueue = new LinkedBlockingQueue<>();
    private final ExecutorService inputProcessor = Executors.newSingleThreadExecutor();

    public InputHandler() {
        keyListener = new KeyListener(this::enqueue);
        serialInputListener = new SerialListener("COM3", 115200, this::enqueue);
    }

    public void start() {
        startGlobalScreen();

        GlobalScreen.addNativeKeyListener(keyListener);
        serialInputListener.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        startProcessingLoop();
    }

    public void stop() {
        stopGlobalScreen();

        serialInputListener.stop();
        inputProcessor.shutdownNow();
    }

    private void startProcessingLoop() {
        inputProcessor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Input input = inputQueue.take();
                    onInput(input);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    private void enqueue(Input input) {
        inputQueue.offer(input);
    }

    private void onInput(Input input) {
        System.out.println(input.getType());
        System.out.println(input.getState());
        System.out.println(input.getKey());
    }

    private void startGlobalScreen() {
        Logger.getLogger(GlobalScreen.class.getPackageName()).setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            Main.LOGGER.severe(e.getMessage());
        }
    }

    private void stopGlobalScreen() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            Main.LOGGER.severe(e.getMessage());
        }
    }
}
