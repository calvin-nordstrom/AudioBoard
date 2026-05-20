package com.calvinnordstrom.audioboard.input;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class SerialListener implements SerialPortDataListener {
    private final SerialPort port;
    private final Consumer<Input> onInput;
    private final StringBuilder buffer = new StringBuilder();

    public SerialListener(String portName, int baudRate, Consumer<Input> onInput) {
        port = SerialPort.getCommPort(portName);
        port.setBaudRate(baudRate);
        port.setParity(SerialPort.NO_PARITY);

        this.onInput = onInput;
    }

    public void start() {
        if (!port.openPort()) {
            return;
        }

        port.addDataListener(this);
    }

    public void stop() {
        port.removeDataListener();
        port.closePort();
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }

        byte[] readBuffer = new byte[port.bytesAvailable()];
        int numRead = port.readBytes(readBuffer, readBuffer.length);

        if (numRead <= 0) {
            return;
        }

        String chunk = new String(readBuffer, 0, numRead, StandardCharsets.UTF_8);
        buffer.append(chunk);

        int index;
        while ((index = buffer.indexOf("\n")) != -1) {
            String line = buffer.substring(0, index).trim();
            buffer.delete(0, index + 1);

            if (!line.isEmpty()) {
                Input input = parseSerialLine(line);
                if (input != null) {
                    onInput.accept(input);
                }
            }
        }
    }

    private Input parseSerialLine(String line) {
        char source = line.charAt(0);
        char state = line.charAt(1);
        String key = line.substring(2);

        Input.Source inputSource;
        if (source == 'K') {
            inputSource = Input.Source.KEYPAD;
        } else if (source == 'D') {
            inputSource = Input.Source.DESKTOP;
        } else {
            return null;
        }

        Input.State inputState;
        if (state == 'P') {
            inputState = Input.State.PRESSED;
        } else if (state == 'R') {
            inputState = Input.State.RELEASED;
        } else {
            return null;
        }

        return new Input(inputSource, inputState, key);
    }
}
