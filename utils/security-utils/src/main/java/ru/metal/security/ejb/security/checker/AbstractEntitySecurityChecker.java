package ru.metal.security.ejb.security.checker;

import ru.metal.security.ejb.AccessCheckException;

import java.lang.reflect.Method;

public abstract class AbstractEntitySecurityChecker<T> implements SecurityChecker<T> {

    @Override
    public boolean check(Method m, Object[] parameters) {
        final String guid = getEntityGUID(m, parameters);
        if (guid == null) {
            throw new AccessCheckException("Error security check: entity guid is NULL for " + m.getName() + " request");
        }

        T entity = getEntity(guid);
        if (entity == null) {
            throw new AccessCheckException("Error security check: entity not found for " + m.getName() + " request (guid=" + guid + ")");
        }

        return check(entity, getEntityOperation(m, parameters));
    }

    /**
     * Из запроса получить гуид сущности
     *
     * @param m          метод
     * @param parameters параметры
     * @return гуид
     */
    protected abstract String getEntityGUID(Method m, Object[] parameters);

    /**
     * Из запроса получить операцию над сущностью
     *
     * @param m          метод
     * @param parameters параметры
     * @return операция
     */
    protected abstract EntityOperation getEntityOperation(Method m, Object[] parameters);

    /**
     * По гуиду получить сущность
     *
     * @param guid гуид
     * @return сущность
     */
    protected abstract T getEntity(String guid);

}
