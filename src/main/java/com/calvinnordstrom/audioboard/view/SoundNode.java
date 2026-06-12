package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.audio.SoundDefinition;
import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.util.Resources;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.nio.file.Path;

public class SoundNode {
    private final SoundDefinition sound;
    private final VBox view = new VBox();

    public SoundNode(SoundDefinition sound) {
        this.sound = sound;

        init();
    }

    private void init() {
        ObjectProperty<Path> iconFile = sound.iconPathProperty();
        ImageView imageView = new ImageView(Resources.getImage(iconFile.get()));
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setSmooth(true);
        iconFile.addListener((_, _, newValue) -> imageView.setImage(Resources.getImage(newValue)));

        Label nameLabel = new Label(sound.getName());
        nameLabel.textProperty().bind(sound.nameProperty());

        ObjectProperty<InputBinding> inputBinding = sound.inputBindingProperty();
        Label inputBindingLabel = new Label(inputBinding.get().key());
        inputBinding.addListener((_, _, newValue) -> inputBindingLabel.setText(newValue.key()));

        view.getChildren().addAll(imageView, nameLabel, inputBindingLabel);
    }

    public Node asNode() {
        return view;
    }
}
