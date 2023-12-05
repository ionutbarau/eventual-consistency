package com.lowheap.poc.eventualconsistency.customerservice.repositories;

import com.lowheap.poc.eventualconsistency.customerservice.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
