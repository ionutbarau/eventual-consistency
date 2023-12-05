package com.lowheap.poc.eventualconsistency.customerservice.repositories;


import com.lowheap.poc.eventualconsistency.customerservice.client.events.CustomerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerEventsRepository extends MongoRepository<CustomerEvent, UUID> {
}
