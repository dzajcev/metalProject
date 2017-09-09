package ru.lanit.hcs.rest.utils;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private final ObjectMapper objectMapper;

    public JacksonConfig() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(df);
        objectMapper.getSerializationConfig().withDateFormat(df);
        objectMapper.getDeserializationConfig().withDateFormat(df);
        /*SimpleModule simpleModule = new SimpleModule("HtmlEscapeModule", Version.unknownVersion());
        simpleModule.addSerializer(String.class, new HtmlEscapedStringSerializer());*/
        //objectMapper.registerModule(simpleModule);
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return objectMapper;
    }
}
