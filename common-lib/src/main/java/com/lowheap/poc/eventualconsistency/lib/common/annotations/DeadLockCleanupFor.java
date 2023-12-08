package com.lowheap.poc.eventualconsistency.lib.common.annotations;

/**
 * Marks a method as a dead-lock cleanup step for a list of sagas.
 * User: Ionut Barau (ionutbarau)
 * Project: eventual-consistency
 * Date: 07.12.2023.
 * Time: 11:41
 */
public @interface DeadLockCleanupFor {

    String[] sagas() default "";
}
