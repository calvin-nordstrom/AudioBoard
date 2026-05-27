package com.calvinnordstrom.audioboard.input;

import com.calvinnordstrom.audioboard.audio.AudioCommand;
import com.calvinnordstrom.audioboard.audio.PlaySampleCommand;
import com.calvinnordstrom.audioboard.audio.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InputRouter {
    private final Consumer<AudioCommand> commandSink;
    private final Map<InputTrigger, Sound> bindings = new HashMap<>();

    public InputRouter(Consumer<AudioCommand> commandSink) {
        this.commandSink = commandSink;
    }

    public void route(Input input) {
        if (input.state() != Input.State.PRESSED) {
            return;
        }

//        String sampleId = bindings.get(input.key());
//
//        if (sampleId == null) {
//            return;
//        }

        commandSink.accept(new PlaySampleCommand());
    }
}
