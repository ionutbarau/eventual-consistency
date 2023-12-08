package com.lowheap.poc.eventualconsistency.paymentservice.messaging.consumers.customer;

import com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerRegistrationDeadLockEventPayload;
import com.lowheap.poc.eventualconsistency.lib.common.events.ConsumedEvent;
import com.lowheap.poc.eventualconsistency.lib.common.services.ConsumedEventService;
import com.lowheap.poc.eventualconsistency.paymentservice.model.Payer;
import com.lowheap.poc.eventualconsistency.paymentservice.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerEventType.CUSTOMER_REGISTRATION_DEAD_LOCK_EVENT;

/**
 * User: Ionut Barau (ionutbarau)
 * Project: eventual-consistency
 * Date: 07.12.2023.
 * Time: 10:46
 */
@Service(CUSTOMER_REGISTRATION_DEAD_LOCK_EVENT)
@Slf4j
@Transactional
public class CustomerRegistrationDeadLockEventConsumer implements Consumer<Message<CustomerRegistrationDeadLockEventPayload>> {

    private final PaymentService paymentService;
    private final ConsumedEventService consumedEventService;
    private final ModelMapper modelMapper;

    public CustomerRegistrationDeadLockEventConsumer(PaymentService paymentService, ConsumedEventService consumedEventService, ModelMapper modelMapper) {
        this.paymentService = paymentService;
        this.consumedEventService = consumedEventService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void accept(Message<CustomerRegistrationDeadLockEventPayload> event) {
        String eventId = (String) event.getHeaders().get("eventId");
        //skip an already consumed event
        if (consumedEventService.findByEventId(eventId) != null) {
            log.info("Event with id " + eventId + " already processed");
            return;
        }
        Payer payer = modelMapper.map(event.getPayload(), Payer.class);
        paymentService.revertAuthorizationPayment(payer);
        //save the consumed event
        consumedEventService.save(ConsumedEvent.builder().eventId(eventId).timestamp(LocalDateTime.now()).build());
    }
}
