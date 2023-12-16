package com.lowheap.poc.eventualconsistency.lib.common.annotations;

/**
 * Marks a method as the deadlock detection method for a list of sagas.
 * User: Ionut Barau (ionutbarau)
 * Project: eventual-consistency
 * Date: 07.12.2023.
 * Time: 11:38
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeadLockDetectionFor {

    String[] sagas() default "";
}
