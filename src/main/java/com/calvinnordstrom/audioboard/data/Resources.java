package com.calvinnordstrom.audioboard.data;

import com.calvinnordstrom.audioboard.Main;
import javafx.scene.image.Image;

import java.io.File;
import java.nio.file.Files;
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

    public static Image getImage(File file) {
        if (file != null && Files.isRegularFile(file.toPath())) {
            return new Image(file.toPath().toUri().toString());
        }

        return new Image(DEFAULT_ICON);
    }
}
