package com.lowheap.poc.eventualconsistency.lib.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Marks a method as a transaction that can be compensated (rolled back).
 * Compensable transactions are transactions that can be rolled back by a compensating transaction
 */
public @interface Compensable {
    String[] sagas() default "";
}
