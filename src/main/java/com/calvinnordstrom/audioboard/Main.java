package com.calvinnordstrom.audioboard;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.model.MainModel;
import com.calvinnordstrom.audioboard.util.Resources;
import com.calvinnordstrom.audioboard.view.MainView;
import com.calvinnordstrom.audioboard.viewmodel.MainViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final String TITLE = "AudioBoard";
    private static final String VERSION = "0.1.0";
    private static final double WIDTH = 1440;
    private static final double HEIGHT = 810;
    private static final double MIN_WIDTH = 960;
    private static final double MIN_HEIGHT = 540;
    private final MainModel model = new MainModel();
    private final MainController controller = new MainController(model);
    private final MainViewModel viewModel = new MainViewModel(
            model.getSounds(),
            model.getSettings()
    );
    private final MainView view = new MainView(viewModel, controller);

    @Override
    public void init() {
    }

    @Override
    public void start(Stage stage) {
        model.start();

        Scene scene = new Scene((Parent) view.asNode());
        scene.getStylesheets().add(Resources.STYLES);

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
        model.stop();
    }
}
