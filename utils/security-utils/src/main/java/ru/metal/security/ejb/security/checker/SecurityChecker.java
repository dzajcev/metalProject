package ru.metal.security.ejb.security.checker;

import java.lang.reflect.Method;

public interface SecurityChecker<T> {

    /**
     *  Проверить, доступен ли пользователю метод с вызываемыми параметрами
     *
     * @param m метод
     * @param parameters параметры
     * @return признак
     */
    public boolean check(Method m, Object[] parameters);

    /**
     *  Проверить, доступна ли данная операция над данной сущностью текущему пользователю
     *
     * @param entity сущность
     * @param operation операция
     * @return признак
     */
    public boolean check(T entity, EntityOperation operation);

}
