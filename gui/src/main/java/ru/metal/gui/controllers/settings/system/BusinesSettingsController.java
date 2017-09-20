package ru.metal.gui.controllers.settings.system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.common.api.response.AbstractResponse;
import ru.common.api.response.ObtainEmailSettingsResponse;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.windows.SaveButton;
import ru.metal.rest.AdminClient;
import ru.metal.rest.ApplicationSettingsSingleton;

/**
 * Created by User on 08.08.2017.
 */
public class BusinesSettingsController extends AbstractController {
//    @FXML
//    private SaveButton save;
    @FXML
    private Label smtpHost;
    @FXML
    private Label smtpPort;
    @FXML
    private Label sender;
    @FXML
    private Label login;
    @FXML
    private Label pass;

    @FXML
    private void initialize() {
        AdminClient adminClient = new AdminClient();

        ObtainEmailSettingsResponse obtainEmailSettingsResponse = adminClient.getEmailSettings();

        smtpHost.setText(obtainEmailSettingsResponse.getEmailSettings().getSmtpHost());
        smtpPort.setText(obtainEmailSettingsResponse.getEmailSettings().getSmtpPort().toString());
        sender.setText(obtainEmailSettingsResponse.getEmailSettings().getFrom());
        login.setText(obtainEmailSettingsResponse.getEmailSettings().getUserName());
        pass.setText(obtainEmailSettingsResponse.getEmailSettings().getPassword());
      //  registerControlsProperties(save);
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        save();
    }


    @Override
    protected AbstractResponse save() {
        return null;
    }
}
