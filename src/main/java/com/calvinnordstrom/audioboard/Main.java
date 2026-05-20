package com.calvinnordstrom.audioboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static final String TITLE = "AudioBoard";
    private static final String VERSION = "0.1.0";
    private static final double WIDTH = 960;
    private static final double HEIGHT = 540;
    private static final double MIN_WIDTH = 640;
    private static final double MIN_HEIGHT = 360;

    @Override
    public void start(Stage stage) {
        Node root = new BorderPane();
        Scene scene = new Scene((Parent) root);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("styles.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle(TITLE + " " + VERSION);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setOnHidden(_ -> Platform.exit());
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
