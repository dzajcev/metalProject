package ru.metal.gui.windows.icons;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.metal.gui.windows.LabelButton;

/**
 * Created by User on 07.08.2017.
 */
public class MaximizeIcon extends LabelButton {
    Image expand = new Image(getClass().getResourceAsStream("/icons/expand.png"));
    Image dexand = new Image(getClass().getResourceAsStream("/icons/dexand.png"));

    public MaximizeIcon(BooleanProperty maximized) {
        super();
        if (maximized.getValue()) {
            setGraphic(new ImageView(dexand));
        } else {
            setGraphic(new ImageView(expand));
        }
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (maximized.getValue()) {
                    setGraphic(new ImageView(expand));
                } else {
                    setGraphic(new ImageView(dexand));
                }
                maximized.setValue(!maximized.getValue());
            }
        });
    }
}
