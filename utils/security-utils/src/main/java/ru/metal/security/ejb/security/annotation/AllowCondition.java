package ru.metal.security.ejb.security.annotation;

import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface AllowCondition {

    /**
     * @return необходимые привилегии
     */
    Privilege[] privilege() default {};

    /**
     * @return необходимые полномочия пользователя
     */
    Role[] userRole() default {};

    /**
     * @return имена Named-бинов (имплементаций SecurityChecker), содержащих логику проверок прав
     */
    String[] checker() default {};

}
