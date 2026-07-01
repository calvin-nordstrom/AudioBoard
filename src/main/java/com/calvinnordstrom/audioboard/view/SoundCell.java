package com.calvinnordstrom.audioboard.view;

import com.calvinnordstrom.audioboard.input.InputBinding;
import com.calvinnordstrom.audioboard.viewmodel.SoundListViewModel;
import com.calvinnordstrom.audioboard.viewmodel.SoundViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.nio.file.Path;

import static com.calvinnordstrom.audioboard.util.Resources.getImage;

public class SoundCell {
    private static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
    private final SoundViewModel item;
    private final SoundListViewModel listModel;
    private final VBox view = new VBox();
    private final ImageView iconImage = new ImageView();
    private final Label nameLabel = new Label();
    private final Label inputBindingLabel = new Label();
    private final HBox iconPane = new HBox(iconImage);
    private final HBox namePane = new HBox(nameLabel);
    private final HBox inputBindingPane = new HBox(inputBindingLabel);

    public SoundCell(SoundViewModel item, SoundListViewModel listModel) {
        this.item = item;
        this.listModel = listModel;
        init();
    }

    private void init() {
        iconImage.imageProperty().unbind();
        ObjectProperty<Path> iconPath = item.iconPathProperty();
        iconImage.setImage(getImage(iconPath.get()));
        iconPath.addListener((_, _, newValue) -> iconImage.setImage(getImage(newValue)));

        nameLabel.textProperty().unbind();
        nameLabel.textProperty().bind(item.nameProperty());

        inputBindingLabel.textProperty().unbind();
        inputBindingLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> {
                            InputBinding binding = item.inputBindingProperty().get();

                            if (binding == null) {
                                return "Unbound";
                            }

                            return binding.key() + " (" + binding.source() + ")";
                        },
                        item.inputBindingProperty()
                )
        );

        view.setOnMousePressed(e -> listModel.selectedSoundProperty().set(item));

        view.pseudoClassStateChanged(
                SELECTED,
                listModel.selectedSoundProperty().get() == item
        );

        listModel.selectedSoundProperty().addListener((_, _, selected) -> {
                    view.pseudoClassStateChanged(SELECTED, selected == item);
                }
        );

        view.getChildren().addAll(
                iconPane,
                namePane,
                inputBindingPane
        );

        // Styles

        iconImage.setFitWidth(100);
        iconImage.setFitHeight(100);
        iconImage.setSmooth(true);

        HBox.setHgrow(namePane, Priority.ALWAYS);
        HBox.setHgrow(inputBindingPane, Priority.ALWAYS);

        view.getStyleClass().add("sound-cell");
        iconPane.getStyleClass().add("sound-cell-icon-pane");
        namePane.getStyleClass().add("sound-cell-name-pane");
        inputBindingPane.getStyleClass().add("sound-cell-input-binding-pane");
    }

//    private void init() {
//        // Styles
//
//        iconImage.setFitWidth(100);
//        iconImage.setFitHeight(100);
//        iconImage.setSmooth(true);
//
//        HBox.setHgrow(nameLabelPane, Priority.ALWAYS);
//        HBox.setHgrow(inputBindingLabelPane, Priority.ALWAYS);
//
//        iconImage.getStyleClass().add("sound-cell-icon-image");
//        nameLabelPane.getStyleClass().add("sound-cell-name-label");
//        inputBindingLabelPane.getStyleClass().add("sound-cell-input-binding-label");
//    }

//    @Override
//    protected void updateItem(SoundViewModel item, boolean empty) {
//        super.updateItem(item, empty);
//
//        if (empty || item == null) {
//            setGraphic(null);
//            return;
//        }
//
//        iconImage.imageProperty().unbind();
//        ObjectProperty<Path> iconPath = item.iconPathProperty();
//        iconImage.setImage(getImage(iconPath.get()));
//        iconPath.addListener((_, _, newValue) -> iconImage.setImage(getImage(newValue)));
//
//        nameLabel.textProperty().unbind();
//        nameLabel.textProperty().bind(item.nameProperty());
//
//        inputBindingLabel.textProperty().unbind();
//        inputBindingLabel.textProperty().bind(
//                Bindings.createStringBinding(
//                        () -> {
//                            InputBinding binding = item.inputBindingProperty().get();
//                            if (binding == null) {
//                                return "Unbound";
//                            }
//
//                            return binding.key() + " (" + binding.source() + ")";
//                        },
//                        item.inputBindingProperty()
//                )
//        );
//
//        setGraphic(view);
//    }

    public Node asNode() {
        return view;
    }
}
