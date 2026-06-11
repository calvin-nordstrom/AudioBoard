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
                Input input = decodeSerialLine(line);
                if (input != null) {
                    onInput.accept(input);
                }
            }
        }
    }

    private static Input decodeSerialLine(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }

        char sourceChar = line.charAt(0);
        char stateChar = line.charAt(1);
        String key = line.substring(2);

        Input.Source source = switch (sourceChar) {
            case 'K' -> Input.Source.KEYPAD;
            case 'D' -> Input.Source.DESKTOP;
            default -> null;
        };

        Input.State state = switch (stateChar) {
            case 'P' -> Input.State.PRESSED;
            case 'R' -> Input.State.RELEASED;
            default -> null;
        };

        if (source == null || state == null || key.isEmpty()) {
            return null;
        }

        return new Input(source, state, key);
    }
}
