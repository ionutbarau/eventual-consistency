package com.lowheap.poc.eventualconsistency.customerservice.client.events;

import com.lowheap.poc.eventualconsistency.lib.common.events.DomainEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customer-events")
@SuperBuilder
@Data
@NoArgsConstructor
public abstract class CustomerEvent extends DomainEvent {
    private static final long serialVersionUID = -2468469840737299150L;
}
