package com.lowheap.poc.eventualconsistency.lib.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Marks a method as being the start of a saga
 */
public @interface SagaStart {
    String[] sagas() default "";
}
