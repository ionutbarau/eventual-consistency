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
public class CustomerNotRegisteredEventPayload implements Serializable {

    private static final long serialVersionUID = 5741498204929111108L;
    private CustomerDto customerDto;
    private String reason;
}
