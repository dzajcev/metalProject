package ru.lanit.hcs.convert.converters;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerConverter;

public class StringDozerConverter extends DozerConverter<String, String> {

    public StringDozerConverter() {
        super(String.class, String.class);
    }

    @Override
    public String convertTo(String source, String destination) {
        return getObject(source);
    }

    @Override
    public String convertFrom(String source, String destination) {
        return getObject(source);
    }

    private String getObject(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source;
        } else {
            return null;
        }
    }
}
