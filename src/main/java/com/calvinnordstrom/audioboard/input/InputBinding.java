package com.calvinnordstrom.audioboard.input;

import java.io.Serializable;

public record InputBinding(Input.Source source, String key) implements Serializable {
}
