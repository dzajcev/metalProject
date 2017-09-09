package ru.metal.dto.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by User on 02.09.2017.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValidatableCollection {
    int maxSize() default 1000;

    int minSize() default 0;

    String predicateName() default "";
}
