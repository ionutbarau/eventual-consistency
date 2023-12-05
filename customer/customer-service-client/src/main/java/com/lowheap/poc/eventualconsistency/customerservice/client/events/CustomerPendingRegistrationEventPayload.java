package com.lowheap.poc.eventualconsistency.customerservice.client.events;

import com.lowheap.poc.eventualconsistency.customerservice.client.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPendingRegistrationEventPayload implements Serializable {
    private static final long serialVersionUID = 3489319978114562112L;
    private CustomerDto customerDto;
}
