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
        MenuItem fileExit = new MenuItem("Exit");
        fileExit.setOnAction(_ -> Platform.exit());

        Menu fileMenu = new Menu(
                "File",
                null,
                fileExit
        );

        view.getMenus().addAll(
                fileMenu
        );
    }

    public Node asNode() {
        return view;
    }
}
