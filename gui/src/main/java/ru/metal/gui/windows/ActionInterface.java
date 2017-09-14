package ru.metal.gui.windows;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * Created by User on 30.08.2017.
 */
public interface ActionInterface {

    <T extends Event> void addCustomEventHandler(
            final EventType<T> eventType,
            final EventHandler<T> eventHandler);

    void setDisable(boolean disable);

}
