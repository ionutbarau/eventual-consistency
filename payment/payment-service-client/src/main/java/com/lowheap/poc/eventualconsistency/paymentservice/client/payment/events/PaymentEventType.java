package com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events;

public interface PaymentEventType {

    String PAYMENT_AUTHORIZED_EVENT = "PaymentAuthorizedEvent";
    String PAYMENT_NOT_AUTHORIZED_EVENT = "PaymentNotAuthorizedEvent";
}
