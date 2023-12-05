package com.lowheap.poc.eventualconsistency.paymentservice;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.lowheap.poc"})
public class PaymentServiceApplication {

    @Value("#{'${interestingEvents}'.split(';')}")
    private List<String> interestingEvents;


    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Helps in deserializing custom headers
     */
    @Bean
    public KafkaHeaderMapper defaultKafkaHeaderMapper(){
        DefaultKafkaHeaderMapper defaultKafkaHeaderMapper = new DefaultKafkaHeaderMapper();
        defaultKafkaHeaderMapper.setRawMappedHeaders(Map.of("eventType", true, "eventId", true, KafkaHeaders.RECEIVED_KEY, true));
        return defaultKafkaHeaderMapper;
    }

    /**
     * Route by eventType header.The name of the consumer bean should be the event type.
     */
    @Bean
    public MessageRoutingCallback customRouter() {
        return new MessageRoutingCallback() {
            @Override
            public String routingResult(Message<?> message) {
                String eventType = (String) message.getHeaders().get("eventType");
                if (!interestingEvents.contains(eventType)) {
                    return "NotInterestedEvent";
                }
                return eventType;
            }
        };
    }

    @Bean
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory factory) {
        return new MongoTransactionManager(factory);
    }


}
