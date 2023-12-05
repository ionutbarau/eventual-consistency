package com.lowheap.poc.eventualconsistency.paymentservice.repositories;

import com.lowheap.poc.eventualconsistency.paymentservice.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
}
