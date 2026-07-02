package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.controller.MainController;
import com.calvinnordstrom.audioboard.viewmodel.MainViewModel;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarView {
    private final MainViewModel model;
    private final MainController controller;
    private final MenuBar view = new MenuBar();

    public MenuBarView(MainViewModel model, MainController controller) {
        this.model = model;
        this.controller = controller;

        initFileMenu();
    }

    private void initFileMenu() {
        MenuItem newSound = new MenuItem("New sound...");
        newSound.setOnAction(_ -> {
            SoundCreatorView soundCreator = new SoundCreatorView(
                    model.getSounds(),
                    view.getScene() != null
                            ? view.getScene().getWindow()
                            : null
            );
            soundCreator.show();
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(_ -> Platform.exit());

        Menu menu = new Menu(
                "File",
                null,
                newSound,
                exit
        );

        view.getMenus().addAll(
                menu
        );
    }

    public Node asNode() {
        return view;
    }
}
