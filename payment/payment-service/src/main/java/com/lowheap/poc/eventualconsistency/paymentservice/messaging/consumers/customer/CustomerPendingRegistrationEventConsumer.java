package com.lowheap.poc.eventualconsistency.paymentservice.messaging.consumers.customer;

import com.lowheap.poc.eventualconsistency.customerservice.client.dto.CustomerDto;
import com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerPendingRegistrationEventPayload;
import com.lowheap.poc.eventualconsistency.lib.common.events.ConsumedEvent;
import com.lowheap.poc.eventualconsistency.lib.common.services.ConsumedEventService;
import com.lowheap.poc.eventualconsistency.paymentservice.model.Customer;
import com.lowheap.poc.eventualconsistency.paymentservice.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerEventType.CUSTOMER_PENDING_REGISTRATION_EVENT;


@Service(CUSTOMER_PENDING_REGISTRATION_EVENT)
@Slf4j
@Transactional
public class CustomerPendingRegistrationEventConsumer implements Consumer<Message<CustomerPendingRegistrationEventPayload>> {

    private final PaymentService paymentService;
    private final ConsumedEventService consumedEventService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerPendingRegistrationEventConsumer(PaymentService paymentService, ConsumedEventService consumedEventService, ModelMapper modelMapper) {
        this.paymentService = paymentService;
        this.consumedEventService = consumedEventService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void accept(Message<CustomerPendingRegistrationEventPayload> event) {
        String eventId = (String) event.getHeaders().get("eventId");
        //skip an already consumed event
        if (consumedEventService.findByEventId(eventId) != null) {
            log.info("Event with id " + eventId + " already processed");
            return;
        }
        CustomerDto customer = event.getPayload().getCustomerDto();
        paymentService.authorizeCustomer(modelMapper.map(customer, Customer.class));
        //save the consumed event
        consumedEventService.save(ConsumedEvent.builder().eventId(eventId).timestamp(LocalDateTime.now()).build());
    }
}
