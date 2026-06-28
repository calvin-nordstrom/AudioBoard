package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.util.Resources;
import com.calvinnordstrom.audioboard.viewmodel.SoundViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.nio.file.Path;

public class SoundCell extends ListCell<SoundViewModel> {
    private final ImageView image = new ImageView();
    private final Label name = new Label();
    private final Label binding = new Label();
    private final VBox view = new VBox(image, name, binding);

    @Override
    protected void updateItem(SoundViewModel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        image.imageProperty().unbind();
        ObjectProperty<Path> iconPath = item.iconPathProperty();
        image.setImage(Resources.getImage(iconPath.get()));
        iconPath.addListener((_, _, newValue) -> {
            image.setImage(Resources.getImage(newValue));
        });

        name.textProperty().unbind();
        name.textProperty().bind(item.nameProperty());

        binding.textProperty().unbind();
        binding.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            InputBinding b =
                                    item.inputBindingProperty().get();
                            return b == null ? "" : b.key();
                        },
                        item.inputBindingProperty()));

        setGraphic(view);
    }
}
