package com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuthorizationFailedEvent extends PaymentEvent{
   private static final long serialVersionUID = -4472385694076309249L;
   private PaymentAuthorizationFailedEventPayload payload;
}
