package com.lowheap.poc.eventualconsistency.customerservice.messaging.consumers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service("NotInterestedEvent")
@Slf4j
public class NotInterestedConsumer implements Consumer<Message<Object>> {
    @Override
    public void accept(Message<Object> objectMessage) {
        log.info("Not interested about event :" + objectMessage.getHeaders().get("eventType"));
    }
}
