package com.calvinnordstrom.audioboard.input;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class KeyListener implements NativeKeyListener {
    private final Consumer<Input> onInput;
    private final Set<Integer> activeKeys = new HashSet<>();

    public KeyListener(Consumer<Input> onInput) {
        this.onInput = onInput;
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param event the native key event.
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {}

    /**
     * Invoked when a key has been pressed.
     *
     * @param event the native key event.
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        int keyCode = event.getKeyCode();

        if (!activeKeys.contains(keyCode)) {
            onInput.accept(new Input(
                    Input.Source.DESKTOP,
                    Input.State.PRESSED,
                    NativeKeyEvent.getKeyText(keyCode)
            ));
        }

        activeKeys.add(keyCode);
    }

    /**
     * Invoked when a key has been released.
     *
     * @param event the native key event.
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        int keyCode = event.getKeyCode();

        activeKeys.remove(keyCode);

        onInput.accept(new Input(
                Input.Source.DESKTOP,
                Input.State.RELEASED,
                NativeKeyEvent.getKeyText(keyCode)
        ));
    }
}
