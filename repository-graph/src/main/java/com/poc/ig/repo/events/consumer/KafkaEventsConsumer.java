package com.poc.ig.repo.events.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.repo.dto.UserResourceEntitlement;
import com.poc.ig.repo.events.Events;
import com.poc.ig.repo.events.KafkaTopics;
import com.poc.ig.repo.events.dto.CertificationDto;
import com.poc.ig.repo.events.dto.EntitlementsPublishedEventDto;
import com.poc.ig.repo.events.dto.Event;
import com.poc.ig.repo.events.dto.UserResourceEntitlements;
import com.poc.ig.repo.events.producer.EntitlementsProducer;
import com.poc.ig.repo.events.producer.RepositoryEventsProducer;

@Component
public class KafkaEventsConsumer {

	@Autowired
	private EntitlementsProducer entitlementsProducer;
	
	@Autowired
	private RepositoryEventsProducer repositoryEventsProducer;
	
	@Autowired
	private ObjectMapper jsonObjectMapper;
	
	private static final int BATCH_SIZE = 5;

	@KafkaListener(containerFactory = "kafkaEventsListenerContainerFactory", topics = KafkaTopics.CERTIFICATION_EVENT_TOPIC)
	public void listen(@SuppressWarnings("rawtypes") Event event) {

		if (event.getName().equals(Events.NEW_CERTITICATION_EVENT)) {
			CertificationDto certification = (CertificationDto) event.getEventData(jsonObjectMapper);

			List<UserResourceEntitlement> entitlementsIn = entitlementsProducer.getUserResourceEntitlementsEvent(
					certification.getTenantName(), certification.getOrganization().getName());
			int entitlementsSize = entitlementsIn.size();
			UserResourceEntitlements entitlementsOut = null;
			Event<UserResourceEntitlements> userResourceEntitlementsEvent = null;
			for (int i = 0; i < entitlementsSize; i++) {
				if (entitlementsOut == null) {
					entitlementsOut = new UserResourceEntitlements();
					entitlementsOut.setTenantName(certification.getTenantName());
					entitlementsOut.setOrganizationName(certification.getOrganization().getName());
					entitlementsOut.setCertificationName(certification.getName());
				}
				entitlementsOut.getEntitlements().add(entitlementsIn.get(i));
				if (i % BATCH_SIZE == 0) {
					userResourceEntitlementsEvent = new Event<UserResourceEntitlements>(Events.USER_RESOURCE_ENTITLEMENTS_EVENT, entitlementsOut, jsonObjectMapper);
					entitlementsProducer.send(userResourceEntitlementsEvent);
				}
			}
			if (entitlementsOut != null) {
				userResourceEntitlementsEvent = new Event<UserResourceEntitlements>(Events.USER_RESOURCE_ENTITLEMENTS_EVENT, entitlementsOut, jsonObjectMapper);
				entitlementsProducer.send(userResourceEntitlementsEvent);
			}
			
			EntitlementsPublishedEventDto dto = new EntitlementsPublishedEventDto();
			dto.setCertificationName(certification.getName());
			dto.setTenantName(certification.getTenantName());
			dto.setEntitlementsCount(entitlementsSize);
			Event<EntitlementsPublishedEventDto> entitlementsPublishedEvent = new Event<EntitlementsPublishedEventDto>(Events.ENTITLEMENTS_PUBLISHED_EVENT, dto, jsonObjectMapper);
			repositoryEventsProducer.send(entitlementsPublishedEvent);
		}

	}
}
