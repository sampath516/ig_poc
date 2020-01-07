package com.poc.ig.repo.events.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.poc.ig.repo.events.KafkaTopics;
import com.poc.ig.repo.events.dto.Event;

@Configuration
public class KafkaEventsProducerConfig {
	
	public static final String KAFKA_HOST_NAME =  "gunsa05kafka.dhcp.broadcom.net:9092";

	@Bean
	ProducerFactory<String, Event> eventsProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST_NAME);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(ProducerConfig.ACKS_CONFIG, "all");
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, Event> eventsKafkaTemplate() {
		return new KafkaTemplate<>(eventsProducerFactory());
	}
	
    @Bean
    public NewTopic certificationEventsTopic() {
         return new NewTopic(KafkaTopics.ENTITLEMENTS_EVENT_TOPIC, 1, (short) 1);
    }
    
    @Bean
    public NewTopic repositoryEventsTopic() {
         return new NewTopic(KafkaTopics.REPOSITORY_EVENT_TOPIC, 1, (short) 1);
    }

}
