package com.lowheap.poc.eventualconsistency.customerservice.client.events;

import com.lowheap.poc.eventualconsistency.customerservice.client.dto.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * User: Ionut Barau (ionutbarau)
 * Project: eventual-consistency
 * Date: 07.12.2023.
 * Time: 10:30
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationDeadLockEvent extends CustomerEvent{
    @Serial
    private static final long serialVersionUID = -7814993460130951188L;
    private CustomerRegistrationDeadLockEventPayload payload;
}
