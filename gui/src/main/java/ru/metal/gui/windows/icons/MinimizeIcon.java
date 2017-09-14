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
public class MinimizeIcon extends LabelButton {

    public MinimizeIcon(BooleanProperty minimized) {
        Image imageClose = new Image(getClass().getResourceAsStream("/icons/minimize.png"));
        setGraphic(new ImageView(imageClose));
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                minimized.setValue(!minimized.getValue());
            }
        });

    }
}
