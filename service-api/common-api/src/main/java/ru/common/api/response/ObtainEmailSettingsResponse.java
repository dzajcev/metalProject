package ru.common.api.response;

import ru.common.api.dto.EmailSettings;
import ru.common.api.response.AbstractResponse;

/**
 * Created by User on 18.09.2017.
 */
public class ObtainEmailSettingsResponse extends AbstractResponse {

    private EmailSettings emailSettings;

    public EmailSettings getEmailSettings() {
        return emailSettings;
    }

    public void setEmailSettings(EmailSettings emailSettings) {
        this.emailSettings = emailSettings;
    }
}
