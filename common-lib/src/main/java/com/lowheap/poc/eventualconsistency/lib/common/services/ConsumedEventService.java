package com.lowheap.poc.eventualconsistency.lib.common.services;

import com.lowheap.poc.eventualconsistency.lib.common.events.ConsumedEvent;

/**
 * User: Ionut Barau (ionutbarau)
 * Project: microservice-poc
 * Date: 03.10.2022.
 * Time: 21:18
 */
public interface ConsumedEventService {
    ConsumedEvent save(ConsumedEvent consumedEvent);

    ConsumedEvent findByEventId(String eventId);

    void cleanup();
}
