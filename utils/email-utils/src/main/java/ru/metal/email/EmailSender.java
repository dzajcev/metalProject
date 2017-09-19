package ru.metal.email;
import org.dozer.MappingException;

import java.util.Collection;
import java.util.List;

public interface EmailSender {
    boolean send(Email email);

    boolean saveProperties(EmailProperty emailProperty);

    EmailProperty getEMailProperty();


}
