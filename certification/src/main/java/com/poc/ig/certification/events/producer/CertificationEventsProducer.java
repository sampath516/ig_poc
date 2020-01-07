package com.poc.ig.certification.events.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.poc.ig.certification.events.KafkaTopics;
import com.poc.ig.certification.events.dto.Event;

@Component
public class CertificationEventsProducer {
	
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;
	
	public void send(Event event) {
		kafkaTemplate.send(KafkaTopics.CERTIFICATION_EVENT_TOPIC, event);
	}

}
