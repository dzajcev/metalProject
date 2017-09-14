package ru.metal.convert.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.dozer.DozerConverter;

public class CalendarToStringConverter extends DozerConverter<Calendar, String>{
    // такой формат даты установлен в RestEasy
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private final static DateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    private final static Calendar calendar = Calendar.getInstance();
    
    public CalendarToStringConverter(Class<Calendar> prototypeA, Class<String> prototypeB) {
	super(prototypeA, prototypeB);
    }

    public CalendarToStringConverter() {
	super(Calendar.class, String.class);
    }


    @Override
    public String convertTo(Calendar source, String destination) {
	return source != null ? simpleDateFormat.format(source.getTime()) : null;
    }

    @Override
    public Calendar convertFrom(String source, Calendar destination) {
	Date date = null;
	try {
	    date = simpleDateFormat.parse(source);
	    calendar.setTime(date);
	    calendar.getTime();
	} catch (ParseException pe) {
	    
	}
	
	return date != null ? calendar : null;
    }

}
