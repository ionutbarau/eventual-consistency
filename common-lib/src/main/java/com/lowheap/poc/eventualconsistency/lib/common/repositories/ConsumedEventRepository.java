package com.lowheap.poc.eventualconsistency.lib.common.repositories;

import com.lowheap.poc.eventualconsistency.lib.common.events.ConsumedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumedEventRepository extends MongoRepository<ConsumedEvent, String> {

    ConsumedEvent findByEventId(String eventId);
}
