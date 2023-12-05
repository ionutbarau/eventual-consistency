package com.lowheap.poc.eventualconsistency.customerservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.lowheap.poc"})
@EnableScheduling
public class CustomerServiceApplication {

	@Value("#{'${interestingEvents}'.split(';')}")
	private List<String> interestingEvents;

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
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

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
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
