package ru.metal.rest;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import ru.metal.api.organizationinfo.dto.OrganizationInfoDto;
import ru.metal.exceptions.ExceptionShower;
import ru.metal.exceptions.ServerErrorException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by User on 08.08.2017.
 */
public abstract class AbstractRestClient {
    public ResteasyWebTarget createTarget(String additionalPath){
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager=new PoolingHttpClientConnectionManager();
        CloseableHttpClient closeableHttpClient =
                HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager).build();
        ApacheHttpClient4Engine engine =
                new ApacheHttpClient4Engine(closeableHttpClient);

        ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();

        final ResteasyWebTarget target = client.target(ApplicationSettingsSingleton.getInstance().getServerAddress());
        ResteasyWebTarget path = target.path(additionalPath);
        return path;
    }

    public <T> T execute(String additionalPath, RequestType requestType, Object object, Class<T> resultClass) throws ServerErrorException{
        try {
            return executeQuery(additionalPath, requestType, object, resultClass);
        }catch (ServerErrorException e){
            ExceptionShower.showException("Ошибка доступа к серверу", "Проверьте настройки соединения и повторите попытку");
            throw e;
        }
    }
    private  <T> T executeQuery(String additionalPath, RequestType requestType, Object object, Class<T> resultClass) throws ServerErrorException{
        try {
            ResteasyWebTarget target = createTarget(additionalPath);
            switch (requestType) {
                case GET: {
                    T result = target.request(MediaType.APPLICATION_JSON_TYPE).get(resultClass);
                    return result;
                }
                case POST: {
                    T post = target.request(MediaType.APPLICATION_JSON_TYPE)
                            .post(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE), resultClass);
                    return post;
                }
                default: {
                    return null;
                }
            }
        }catch (Exception e){
            throw new ServerErrorException(e);
        }

    }
}