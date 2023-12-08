package com.lowheap.poc.eventualconsistency.customerservice.client.events;

public interface CustomerEventType {

    String CUSTOMER_PENDING_REGISTRATION_EVENT = "CustomerPendingRegistrationEvent";
    String CUSTOMER_REGISTERED_EVENT = "CustomerRegisteredEvent";
    String CUSTOMER_NOT_REGISTERED_EVENT = "CustomerNotRegisteredEvent";

    String CUSTOMER_REGISTRATION_DEAD_LOCK_EVENT = "CustomerRegistrationDeadLockEvent";
}
