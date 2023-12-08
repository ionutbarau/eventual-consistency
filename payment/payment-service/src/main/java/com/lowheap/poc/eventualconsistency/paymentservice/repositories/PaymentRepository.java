package com.lowheap.poc.eventualconsistency.paymentservice.repositories;

import com.lowheap.poc.eventualconsistency.paymentservice.model.Payer;
import com.lowheap.poc.eventualconsistency.paymentservice.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> deleteByPayer(Payer payer);
}
