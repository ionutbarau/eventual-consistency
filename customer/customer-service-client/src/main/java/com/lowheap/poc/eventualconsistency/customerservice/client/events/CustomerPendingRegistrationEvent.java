package com.lowheap.poc.eventualconsistency.customerservice.client.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPendingRegistrationEvent extends CustomerEvent{
    private static final long serialVersionUID = 5917380947497999655L;
    private CustomerPendingRegistrationEventPayload payload;
}
