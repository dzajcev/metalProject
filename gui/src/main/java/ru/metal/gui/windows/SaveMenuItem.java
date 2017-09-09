package ru.metal.gui.windows;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.MenuItem;

/**
 * Created by User on 30.08.2017.
 */
public class SaveMenuItem extends MenuItem implements ActionInterface {
    public <T extends Event> void addCustomEventHandler(
            final EventType<T> eventType,
            final EventHandler<T> eventHandler){
        super.addEventHandler(eventType,eventHandler);
    }
}
