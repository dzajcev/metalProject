package ru.lanit.hcs.rest.utils;

import java.text.*;
import java.util.Date;
import java.util.Locale;

public class ConcurrentSimpleDateFormat extends Format {
	private static final long serialVersionUID = -6920496651082535244L;

    private final String pattern;
    private final Locale locale;

    private final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>();

    public ConcurrentSimpleDateFormat(String pattern) {
        this(pattern, null);
    }

    public ConcurrentSimpleDateFormat(String pattern, Locale locale) {
        this.pattern = pattern;
        this.locale = locale;
    }

    private SimpleDateFormat getDateFormat() {
        SimpleDateFormat result = dateFormat.get();

        if (result == null) {
            if (locale != null) {
                result = new SimpleDateFormat(pattern, locale);
            } else {
                result = new SimpleDateFormat(pattern);
            }
            dateFormat.set(result);
        }

        return result;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
                               FieldPosition fieldPosition) {
        return getDateFormat().format(obj, toAppendTo, fieldPosition);
    }

    @Override
    public Date parseObject(String source, ParsePosition pos) {
        return (Date) getDateFormat().parseObject(source, pos);
    }

    public Date parse(String text) throws ParseException {
        return (Date) getDateFormat().parseObject(text);
    }

    public void setLenient(boolean lenient)
    {
        getDateFormat().setLenient(lenient);
    }

    public boolean isLenient()
    {
        return getDateFormat().isLenient();
    }
}
