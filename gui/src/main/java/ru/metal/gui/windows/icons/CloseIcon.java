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
public class CloseIcon extends LabelButton {


    public CloseIcon(BooleanProperty closeRequest) {
        super();
        Image imageClose = new Image(getClass().getResourceAsStream("/icons/close.png"));
        setGraphic(new ImageView(imageClose));
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                closeRequest.setValue(false);
                closeRequest.setValue(true);
            }
        });
    }
}
