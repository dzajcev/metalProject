package ru.metal.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

/**
 * Created by User on 02.09.2017.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidatableField {
    boolean nullable() default true;

    String regexp() default "";

    String predicateName() default "";

}
