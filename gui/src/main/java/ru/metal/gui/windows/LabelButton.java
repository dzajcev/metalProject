package ru.metal.gui.windows;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Created by User on 15.08.2017.
 */
public class LabelButton extends Label{
    public LabelButton(){
        setPrefSize(20,20);
        setStyle("-fx-background-radius: 3 3 3 3; -fx-border-radius: 3 3 3 3;" +
                "-fx-background-color: transparent");
        setAlignment(Pos.CENTER);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle("-fx-background-radius: 3 3 3 3; -fx-border-radius: 3 3 3 3;" +
                        "-fx-background-color: grey");;
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle("-fx-background-radius: 3 3 3 3; -fx-border-radius: 3 3 3 3;" +
                        "-fx-background-color: transparent");
            }
        });
        addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("ActionEvent: " + event);
            }
        });
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton()== MouseButton.PRIMARY) {
                    if (onActionProperty().get()!=null) {
                        onActionProperty().get().handle(new ActionEvent());
                    }
                }
            }
        });
    }
    public LabelButton(String text, Image image){
        this();
        if (text!=null){
            setText(text);
        }
        if (image!=null){
            setGraphic(new ImageView(image));
        }
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return LabelButton.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };


}
