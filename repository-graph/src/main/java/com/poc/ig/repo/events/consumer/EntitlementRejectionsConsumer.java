package com.poc.ig.repo.events.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.repo.events.Events;
import com.poc.ig.repo.events.KafkaTopics;
import com.poc.ig.repo.events.dto.EntitlementsRejectedEvent;
import com.poc.ig.repo.events.dto.EntitlementsRejectedEvent.Entitlement;
import com.poc.ig.repo.events.dto.EntitlementsRejectedEvent.EntitlementType;
import com.poc.ig.repo.events.dto.Event;
import com.poc.ig.repo.repository.ResourceRepository;
import com.poc.ig.repo.repository.UserRepository;

@Component
public class EntitlementRejectionsConsumer {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ResourceRepository resRepo;
	
	@Autowired
	ObjectMapper jsonObjectMapper;

	@KafkaListener(containerFactory = "kafkaEventsListenerContainerFactory", topics = KafkaTopics.ENTITLEMENT_REJECTION_EVENT_TOPIC)
	public void listen(Event<EntitlementsRejectedEvent> event) {
		if (event.getName().equals(Events.ENTITLEMENTS_REJECTED_EVENT)) {
			EntitlementsRejectedEvent entRejEvent = event.getEventData(jsonObjectMapper, EntitlementsRejectedEvent.class);
			
			if(entRejEvent.getEntitlementType().equals(EntitlementType.USER_RESOURCE)) {
				rejectUserResourceEntitlements(entRejEvent.getTenant(), entRejEvent.getEntitlements());
			}
		}
	}
		
	private void rejectUserResourceEntitlements(String tenantName, List<Entitlement> entitlements) {		
		for(Entitlement ent : entitlements) {
			userRepo.removeUserResourceRelationship(tenantName, ent.getPrimaryEntityExternalId(), ent.getSecondaryEntityExternalId());
		}
	}
}
