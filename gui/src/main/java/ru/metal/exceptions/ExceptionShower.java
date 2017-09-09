package ru.metal.exceptions;

import javafx.application.Application;
import javafx.scene.control.Alert;

/**
 * Created by User on 09.08.2017.
 */
public class ExceptionShower {
    public static void showException(String title, String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.show();
    }
}
