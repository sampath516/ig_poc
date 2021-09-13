package com.poc.ig.connector.im.events.producer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.poc.ig.connector.im.dto.UserResourceEntitlement;
import com.poc.ig.connector.im.events.KafkaTopics;
import com.poc.ig.connector.im.events.dto.Event;
import com.poc.ig.connector.im.importdata.IMImportService;

@Component
public class EntitlementsProducer {

	@SuppressWarnings("rawtypes")
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;

	@Autowired
	IMImportService imImportService;

	@SuppressWarnings("rawtypes")
	public void send(Event event) {
		kafkaTemplate.send(KafkaTopics.ENTITLEMENTS_EVENT_TOPIC, event);
	}

	public List<UserResourceEntitlement> getUserResourceEntitlementsEvent(String tenantName, String organizationName, String certificationName) {
		return imImportService.getUserResourceEntitlementsEvent(tenantName, organizationName, certificationName);

	}
}
