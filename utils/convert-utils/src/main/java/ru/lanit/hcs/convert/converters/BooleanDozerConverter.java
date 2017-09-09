package ru.lanit.hcs.convert.converters;

import org.dozer.DozerConverter;

public class BooleanDozerConverter extends DozerConverter<Boolean, Boolean> {

    public BooleanDozerConverter() {
        super(Boolean.class, Boolean.class);
    }

    @Override
    public Boolean convertTo(Boolean source, Boolean destination) {
        return getObject(source);
    }

    @Override
    public Boolean convertFrom(Boolean source, Boolean destination) {
        return getObject(source);
    }

    private Boolean getObject(Boolean source) {
        if (source == null || source) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
