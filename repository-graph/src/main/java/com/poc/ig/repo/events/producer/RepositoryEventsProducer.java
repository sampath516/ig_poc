package com.poc.ig.repo.events.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.poc.ig.repo.events.KafkaTopics;
import com.poc.ig.repo.events.dto.Event;

@Component
public class RepositoryEventsProducer {
	
	@SuppressWarnings("rawtypes")
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;

	@SuppressWarnings("rawtypes")
	public void send(Event event) {
		System.out.println("**************Sending REPOSITORY_EVENT_TOPIC event ************************");
		kafkaTemplate.send(KafkaTopics.REPOSITORY_EVENT_TOPIC, event);
	}
}
