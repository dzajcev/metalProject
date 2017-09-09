package ru.lanit.hcs.rest.utils;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class DateParam implements Serializable {
    private static ConcurrentSimpleDateFormat dateFormat = new ConcurrentSimpleDateFormat("dd.MM.yyyy");

    private final Date date;

    public DateParam(String dateStr) throws WebApplicationException {
        if (StringUtils.isBlank(dateStr)) {
            this.date = null;
            return;
        }
        try {
            this.date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Couldn't parse date string: " + e.getMessage())
                    .build());
        }
    }

    public Date getDate() {
        return date;
    }
}
