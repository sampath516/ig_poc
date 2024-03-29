package com.poc.ig.connector.im.events.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.poc.ig.connector.im.events.dto.Event;

@EnableKafka
@Configuration
public class KafkaEventsConsumerConfig {

	public static final String KAFKA_HOST_NAME = "gunsa05kafka.dhcp.broadcom.net:9092";
	public static final String GROUP_ID = "connector-im";

	@SuppressWarnings("rawtypes")
	@Bean
	public ConsumerFactory<String, Event> eventsConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST_NAME);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
	//	props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	//	props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		JsonDeserializer<Event> jsonDeserializer = new JsonDeserializer<Event>(Event.class, false);
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Event> kafkaEventsListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Event> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(eventsConsumerFactory());
		return factory; 
	}

}
