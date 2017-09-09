package ru.metal.rest;

import ru.lanit.hcs.rest.utils.Constants;
import ru.lanit.hcs.rest.utils.JsonResponseModel;
import ru.metal.api.common.request.RequestWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Абстрактный класс REST-сервисов
 *
 * Created by nshablich on 10/23/14.
 */
public class AbstractService {

    @GET
    @Path("echo")
    @Produces("text/plain")
    public String echo() {
        return "echo";
    }

    protected static JsonResponseModel getThrowableJsonResponseModel(Throwable t) {
        final JsonResponseModel jsonResponseModel = new JsonResponseModel(t.getClass().getSimpleName(), new HashMap<String, Object>(1));

        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
        } finally {
            pw.flush();
            jsonResponseModel.getDetails().put("_stackTrace", sw.toString());
            pw.close();
        }
        return jsonResponseModel;
    }




    protected <T> RequestWrapper<T> createRequestWrapper(T dto) {
        RequestWrapper<T> wrapper = new RequestWrapper<>();
        wrapper.setRequest(dto);
        return wrapper;
    }

    protected Map createPaginationResponse(List items) {
        return Collections.singletonMap(Constants.PAGINATION_ITEMS, items);
    }


}
