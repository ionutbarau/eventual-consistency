package com.lowheap.poc.eventualconsistency.customerservice.client.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotRegisteredEvent extends CustomerEvent {
    private static final long serialVersionUID = -4754945414580499078L;
    private CustomerNotRegisteredEventPayload payload;
}
