package com.lowheap.poc.eventualconsistency.paymentservice.repositories;


import com.lowheap.poc.eventualconsistency.paymentservice.client.payment.events.PaymentEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentEventsRepository extends MongoRepository<PaymentEvent, String> {
}
