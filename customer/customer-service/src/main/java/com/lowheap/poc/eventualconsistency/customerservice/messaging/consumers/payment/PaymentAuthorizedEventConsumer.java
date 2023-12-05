package com.lowheap.poc.eventualconsistency.customerservice.messaging.consumers.payment;

import com.lowheap.poc.eventualconsistency.customerservice.services.CustomerService;
import com.lowheap.poc.eventualconsistency.lib.common.events.ConsumedEvent;
import com.lowheap.poc.eventualconsistency.lib.common.services.ConsumedEventService;
import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentAuthorizedEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import static com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentEventType.PAYMENT_AUTHORIZED_EVENT;


@Service(PAYMENT_AUTHORIZED_EVENT)
@Slf4j
@Transactional
public class PaymentAuthorizedEventConsumer implements Consumer<Message<PaymentAuthorizedEventPayload>> {

    private final CustomerService customerService;
    private final ConsumedEventService consumedEventService;

    @Autowired
    public PaymentAuthorizedEventConsumer(CustomerService customerService, ConsumedEventService consumedEventService) {
        this.customerService = customerService;
        this.consumedEventService = consumedEventService;
    }


    @Override
    public void accept(Message<PaymentAuthorizedEventPayload> event) {
        String eventId = (String) event.getHeaders().get("eventId");
        //skip an already consumed event
        if (consumedEventService.findByEventId(eventId) != null) {
            log.info("Event with id " + eventId + " already processed");
            return;
        }
        customerService.approveRegistration(event.getPayload().getCustomerId());
        //save the consumed event
        consumedEventService.save(ConsumedEvent.builder().eventId(eventId).timestamp(LocalDateTime.now()).build());
    }
}
