package ru.lanit.hcs.convert.converters;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerConverter;

/**
 * Created by manosov on 29.09.2014.
 */
public class StringToBooleanConverter extends DozerConverter<String, Boolean> {

    public StringToBooleanConverter() {
        super(String.class, Boolean.class);
    }
    @Override
    public Boolean convertTo(String s, Boolean aBoolean) {
        return StringUtils.isNotBlank(s);
    }

    @Override
    public String convertFrom(Boolean aBoolean, String s) {
        return null;
    }
}
