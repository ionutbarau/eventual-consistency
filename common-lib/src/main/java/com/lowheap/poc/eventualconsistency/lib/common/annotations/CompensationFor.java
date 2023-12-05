package com.lowheap.poc.eventualconsistency.lib.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * Marks a method as the compensating transaction for a compensatable transaction.
 * In this design, there will always be 2 transactions that are marked with @CompensationFor.
 * One will be the transaction that sends the event, and will fill only the compensatableEvents field with the event that triggered the compensation.
 * The second will be the actual operation that does the rollback, and it will fill in the compensatableOperations that needs to be rolled back.
 */
public @interface CompensationFor {


    String[] sagas() default "";
    String[] compensatableEvents() default "";
    String[] compensatableOperations() default "";
}
