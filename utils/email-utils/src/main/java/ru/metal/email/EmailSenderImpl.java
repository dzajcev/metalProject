package ru.metal.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Map;
import java.util.Properties;

@Named
@Singleton
public class EmailSenderImpl implements EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderImpl.class);

    @Inject
    private EmailProperty emailProperty;

    @Override
    public boolean send(Email email) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", emailProperty.getSmtpHost());
        props.put("mail.smtp.port", emailProperty.getSmtpPort());
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailProperty.getUserName(), emailProperty.getPassword());
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProperty.getFrom()));
            int i = 1;
            StringBuilder recipients = new StringBuilder();
            for (String r : email.getRecipients()) {
                recipients.append(r);
                if (i < email.getRecipients().size() - 1) {
                    recipients.append(",");
                }
                i++;
            }
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipients.toString()));
            message.setSubject(email.getTheme());


            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(email.getMessage());

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            for (Map.Entry<String, byte[]> entry : email.getAttachments().entrySet()) {
                messageBodyPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(entry.getValue(), "application/octet-stream");
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(entry.getKey());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            LOGGER.error("произошла ошибка при отправе почты", e);
            return false;
        }
         return true;
    }

    @Override
    public boolean saveProperties(EmailProperty emailProperty) {
        return false;
    }

    @Override
    public EmailProperty getEMailProperty() {
        return null;
    }

}
