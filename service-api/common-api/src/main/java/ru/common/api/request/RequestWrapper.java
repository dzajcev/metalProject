package ru.common.api.request;

/**
 * Запрос к сервису
 */
public class RequestWrapper<E> {
    private E request;

    public E getRequest() {
        return request;
    }

    public void setRequest(E request) {
        this.request = request;
    }
}
