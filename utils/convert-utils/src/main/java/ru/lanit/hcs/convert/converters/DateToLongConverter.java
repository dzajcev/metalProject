package ru.lanit.hcs.convert.converters;

import org.dozer.DozerConverter;
import java.util.Date;

public class DateToLongConverter extends DozerConverter<Date, Long>{
    public DateToLongConverter() {
        super(Date.class, Long.class);
    }

    @Override
    public Long convertTo(Date date, Long mils) {
        return date != null ? date.getTime() : null;
    }

    @Override
    public Date convertFrom(Long mils, Date date) {
        return mils != null ? new Date(mils) : null;
    }
}
