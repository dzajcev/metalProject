package ru.metal.rest;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import ru.common.api.dto.Error;
import ru.common.api.response.AbstractResponse;
import ru.metal.crypto.service.CryptoException;
import ru.metal.dto.ErrorCodeEnum;
import ru.metal.exceptions.ExceptionUtils;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.auth.AuthorizationController;
import ru.metal.rest.providers.RestInterceptor;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by User on 08.08.2017.
 */
public abstract class AbstractRestClient {
    protected ResteasyWebTarget createTarget(String additionalPath) {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient closeableHttpClient =
                HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager).build();
        ApacheHttpClient4Engine engine =
                new ApacheHttpClient4Engine(closeableHttpClient);

        ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
        client.abortIfClosed();
        final ResteasyWebTarget target = client.target(ApplicationSettingsSingleton.getInstance().getServerAddress())
                .register(RestInterceptor.class);

        ResteasyWebTarget path = target.path(additionalPath);
        return path;
    }

    protected <T extends AbstractResponse> T execute(String additionalPath, RequestType requestType, Object object, Class<T> resultClass) {
        return executeQuery(additionalPath, requestType, object, resultClass);
    }
    protected <T extends AbstractResponse> T execute(String additionalPath, RequestType requestType, Class<T> resultClass) {
        return executeQuery(additionalPath, requestType, null, resultClass);
    }
    private <T extends AbstractResponse> T executeQuery(String additionalPath, RequestType requestType, Object object, Class<T> resultClass) {
        ResteasyWebTarget target = createTarget(additionalPath);
        try {
            switch (requestType) {
                case GET: {
                    T result = target.request(MediaType.APPLICATION_JSON_TYPE).get(resultClass);
                    return result;
                }
                case POST: {
                    T post = target.request(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_OCTET_STREAM_TYPE)
                            .post(Entity.entity(object, MediaType.APPLICATION_JSON_TYPE), resultClass);
                    return post;
                }
                default: {
                    return null;
                }
            }
        } catch (Exception e) {
            ObjectProperty<T> response = new SimpleObjectProperty<T>();
            try {
                response.set(resultClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new RuntimeException(e1);
            }
            if (e instanceof ClientErrorException) {
                ClientErrorException ee = (ClientErrorException) e;
                int status = ee.getResponse().getStatus();
                if (status == Response.Status.FORBIDDEN.getStatusCode()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
                    alert.setTitle("Повторная авторизация");
                    alert.setHeaderText("Произошла ошибка при шифровании сообщения.\nСкорее всего, Вы используете не корректный ключ");
                    alert.initOwner(StartPage.primaryStage);
                    alert.setContentText("Закройте приложение, и укажите при авторизации корректные ключи");
                    try {
                        alert.showAndWait();
                        Platform.exit();
                    } catch (Exception ex) {
                    }
                } else if (status == Response.Status.UNAUTHORIZED.getStatusCode()) {
                    response.getValue().getErrors().add(new Error(ErrorCodeEnum.ERR001));
                } else if (status == Response.Status.NOT_ACCEPTABLE.getStatusCode()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                    DialogPane dialogPane = new DialogPane();
                    FXMLLoader loader = new FXMLLoader(StartPage.class.getResource("/fxml/Authorization.fxml"));


                    Label label = new Label("Ваша сессия была не активна более 1 часа.\nТребуется повторная авторизация");
                    VBox vBox = new VBox(label);
                    Parent load = null;
                    AuthorizationController authorizationController = null;
                    try {
                        load = loader.load();
                        authorizationController = loader.getController();
                        authorizationController.setRetry(true);
                        authorizationController.doneProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                if (newValue.equals(AuthorizationController.AUTHORIZATION_ACCEPT)) {
                                    alert.setResult(ButtonType.OK);
                                    alert.close();
                                    response.set(executeQuery(additionalPath, requestType, object, resultClass));
                                }
                            }
                        });
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    vBox.getChildren().add(load);
                    dialogPane.setContent(vBox);
                    alert.setTitle("Повторная авторизация");
                    alert.setDialogPane(dialogPane);
                    alert.setHeaderText(null);
                    alert.initOwner(StartPage.primaryStage);
                    alert.setContentText(null);
                    try {
                        alert.showAndWait();
                    } catch (Exception ex) {
                    }
                } else {
                    throw e;
                }
            } else {
                if (ExceptionUtils.containThrowable(e, CryptoException.class)) {
                    throw new RuntimeException(e);
                } else if (ExceptionUtils.containThrowable(e, ProcessingException.class)){
                    throw new RuntimeException(e);
                } else{
                    response.getValue().getErrors().add(new Error(ErrorCodeEnum.ERR000, e.getMessage()));
                }
            }
            return response.getValue();
        } finally {
            target.getResteasyClient().close();
        }
    }
}
