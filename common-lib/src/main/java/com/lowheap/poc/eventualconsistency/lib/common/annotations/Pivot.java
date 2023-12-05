package com.lowheap.poc.eventualconsistency.lib.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Marks a method as being the go/no go point of a saga. If the pivot is successful, operations that follow the pivot are guaranteed to be successful also.
 * The pivot transaction can be neither compensatable nor retriable. Alternatively it can be the  last compensatable or the first retriable transaction.
 */
public @interface Pivot {
    String[] sagas() default "";
}
