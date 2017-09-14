package ru.metal.gui.controllers.settings.system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.ApplicationSettingsSingleton;

import javax.ejb.Stateless;

/**
 * Created by User on 08.08.2017.
 */
@Stateless
public class BusinesSettingsController extends AbstractController {
    @FXML
    private TextField serverAddress;
    @FXML
    private SaveButton save;

    @FXML
    private void initialize() {
        serverAddress.setText(ApplicationSettingsSingleton.getInstance().getServerAddress());
        registerControlsProperties(save, serverAddress.textProperty());
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        save();
    }


    @Override
    protected boolean save() {
        ApplicationSettingsSingleton.getInstance().setServerAddress(serverAddress.getText());
        ApplicationSettingsSingleton.getInstance().save();
        return true;
    }
}
