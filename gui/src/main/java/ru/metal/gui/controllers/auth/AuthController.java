package ru.metal.gui.controllers.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.Window;

import java.io.IOException;

/**
 * Created by User on 10.09.2017.
 */
public class AuthController {

    @FXML
    private AnchorPane root;

    @FXML
    private void register() {
        FXMLLoader loader = new FXMLLoader(StartPage.class.getResource("/fxml/Register.fxml"));

        Node load = null;
        RegisterController registerController = null;
        try {
            load = loader.load();
            registerController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (load!=null){
            root.getChildren().add(load);
        }
    }
}
