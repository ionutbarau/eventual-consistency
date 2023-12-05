package com.lowheap.poc.eventualconsistency.lib.common.services;

import com.lowheap.poc.eventualconsistency.lib.common.events.ConsumedEvent;
import com.lowheap.poc.eventualconsistency.lib.common.repositories.ConsumedEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * User: Ionut Barau (ionutbarau)
 * Project: microservice-poc
 * Date: 03.10.2022.
 * Time: 21:18
 */
@Service
@Transactional
@Slf4j
public class ConsumedEventServiceImpl implements ConsumedEventService {

    private final ConsumedEventRepository consumedEventRepository;

    @Autowired
    public ConsumedEventServiceImpl(ConsumedEventRepository consumedEventRepository) {
        this.consumedEventRepository = consumedEventRepository;
    }

    @Override
    public ConsumedEvent save(ConsumedEvent consumedEvent) {
        return consumedEventRepository.save(consumedEvent);
    }

    @Override
    public ConsumedEvent findByEventId(String eventId) {
        return consumedEventRepository.findByEventId(eventId);
    }

    @Override
    @Scheduled(cron = "${consumed.events.cleanup.cron}")
    public void cleanup() {
        log.info("Cleaning up consumed events at {}", LocalDateTime.now());
        consumedEventRepository.deleteAll();
    }


}
