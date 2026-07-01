package com.calvinnordstrom.audioboard.util;

import com.calvinnordstrom.audioboard.Main;
import javafx.scene.image.Image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class Resources {
    public static final String DEFAULT_ICON = getResource("icons/default_icon.png");

    private Resources() {
    }

    public static String getResource(String name) {
        return Objects.requireNonNull(
                Main.class.getResource(name),
                () -> "Resource not found: " + name
        ).toExternalForm();
    }

    public static Image getImage(Path path) {
        if (path != null && Files.isRegularFile(path)) {
            return new Image(path.toUri().toString());
        }

        return new Image(DEFAULT_ICON);
    }
}
