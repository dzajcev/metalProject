package ru.metal.security.ejb.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(value = RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface Allow {

    /**
     * @return условия доступа (объединены через ИЛИ, выполение хотя бы одного условия разрешает доступ)
     */
    AllowCondition[] condition() default {};

}
