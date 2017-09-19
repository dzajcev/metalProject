package ru.admin;

import ru.common.api.dto.EmailSettings;
import ru.common.api.response.ObtainEmailSettingsResponse;
import ru.metal.email.EmailProperty;
import ru.metal.email.EmailSender;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.annotation.Allow;
import ru.metal.security.ejb.security.annotation.AllowCondition;
import ru.metal.security.ejb.security.annotation.CheckSecurity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by User on 07.09.2017.
 */
@Path("/emailsettings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@CheckSecurity
@TransactionAttribute(TransactionAttributeType.NEVER)
public class EmailSettingsService {
    @Inject
    private EmailProperty emailProperty;

    @Inject
    private EmailSender emailSender;


    @GET
    @Path("/")
    @Allow(condition = @AllowCondition(userRole = Role.ADMIN))
    public Response get() throws Exception {
        EmailSettings emailSettings = new EmailSettings();
        emailSettings.setFrom(emailProperty.getFrom());
        emailSettings.setPassword(emailProperty.getPassword());
        emailSettings.setSmtpHost(emailProperty.getSmtpHost());
        emailSettings.setSmtpPort(emailProperty.getSmtpPort());
        emailSettings.setUserName(emailProperty.getUserName());
        ObtainEmailSettingsResponse obtainEmailSettingsResponse = new ObtainEmailSettingsResponse();
        obtainEmailSettingsResponse.setEmailSettings(emailSettings);
        return Response.ok(obtainEmailSettingsResponse).build();
    }
}
