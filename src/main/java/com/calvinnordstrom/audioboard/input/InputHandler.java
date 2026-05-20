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
    private final SerialProtocolDecoder decoder = new SerialProtocolDecoder();
    private final BlockingQueue<Input> inputQueue = new LinkedBlockingQueue<>();
    private final ExecutorService inputProcessor = Executors.newSingleThreadExecutor();

    public InputHandler() {
        keyListener = new KeyListener(inputQueue::offer);
        serialInputListener = new SerialListener("COM3", 115200, this::handleSerialLine);
    }

    public void start() {
        startGlobalScreen();

        GlobalScreen.addNativeKeyListener(keyListener);
        serialInputListener.start();

        inputProcessor.submit(this::processLoop);
    }

    public void stop() {
        stopGlobalScreen();

        serialInputListener.stop();
        inputProcessor.shutdownNow();
    }

    private void handleSerialLine(String line) {
        Input input = decoder.decode(line);
        if (input != null) {
            inputQueue.offer(input);
        }
    }

    private void processLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Input input = inputQueue.take();
                onInput(input);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void onInput(Input input) {
        System.out.println(input.source());
        System.out.println(input.state());
        System.out.println(input.key());
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
