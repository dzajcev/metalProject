package ru.report.rest.utils;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.module.SimpleModule;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author d.oskin
 * @since 26.12.2014
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private final ObjectMapper objectMapper;

    public JacksonConfig() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(df);
        objectMapper.getSerializationConfig().withDateFormat(df);
        objectMapper.getDeserializationConfig().withDateFormat(df);
    }

    @Override
    public ObjectMapper getContext(final Class<?> aClass) {
        return objectMapper;
    }
}
