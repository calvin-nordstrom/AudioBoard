package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.Main;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputHandler {
    private static final String INPUT_THREAD_NAME = "AudioBoard Input Thread";
    private final List<Consumer<Input>> listeners = new CopyOnWriteArrayList<>();
    private final BlockingQueue<Input> inputQueue = new LinkedBlockingQueue<>();
    private ExecutorService inputThread;
    private volatile boolean running;
    private final KeyListener keyListener;
    private final SerialListener serialListener;

    public InputHandler() {
        keyListener = new KeyListener(this::handleKeyInput);
        serialListener = new SerialListener("COM3", 115200, this::handleSerialInput);
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;

        inputThread = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setName(INPUT_THREAD_NAME);
            return thread;
        });
        inputThread.submit(this::inputLoop);

        startGlobalScreen();
        GlobalScreen.addNativeKeyListener(keyListener);
        serialListener.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;

        if (inputThread != null) {
            inputThread.shutdownNow();
        }

        stopGlobalScreen();
        serialListener.stop();
    }

    public void addListener(Consumer<Input> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<Input> listener) {
        listeners.remove(listener);
    }

    private void dispatch(Input input) {
        for (Consumer<Input> listener : listeners) {
            listener.accept(input);
        }
    }

    private void handleKeyInput(Input input) {
        inputQueue.offer(input);
    }

    private void handleSerialInput(Input input) {
        inputQueue.offer(input);
    }

    private void inputLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Input input = inputQueue.take();
                dispatch(input);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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
