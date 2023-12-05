package com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuthorizedEvent extends PaymentEvent {
    private static final long serialVersionUID = 3701425076046479598L;
    private PaymentAuthorizedEventPayload payload;
}
