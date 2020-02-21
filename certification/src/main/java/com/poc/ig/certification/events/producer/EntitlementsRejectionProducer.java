package com.poc.ig.certification.events.producer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.certification.entity.Entitlement.EntitlementType;
import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;
import com.poc.ig.certification.events.Events;
import com.poc.ig.certification.events.KafkaTopics;
import com.poc.ig.certification.events.dto.EntitlementsRejectedEvent;
import com.poc.ig.certification.events.dto.EntitlementsRejectedEvent.Entitlement;
import com.poc.ig.certification.events.dto.Event;

@Component
public class EntitlementsRejectionProducer {
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;
	
	public void send(Event<EntitlementsRejectedEvent> event) {
		kafkaTemplate.send(KafkaTopics.ENTITLEMENT_REJECTION_EVENT_TOPIC, event);
	}
	
	
	public void sendEvents(String tenantName, List<com.poc.ig.certification.entity.Entitlement> entitlements, ObjectMapper jsonMapper) {
		EntitlementsRejectedEvent userResRejectEvent = new EntitlementsRejectedEvent();
		userResRejectEvent.setTenant(tenantName);
		userResRejectEvent.setEntitlementType(EntitlementType.USER_RESOURCE);
		for(com.poc.ig.certification.entity.Entitlement userResEntmt: entitlements) {
			if(userResEntmt instanceof UserPrivilegesResourceEntitlement) {
				String primaryEntityExtId = ((UserPrivilegesResourceEntitlement) userResEntmt).getPrimaryEntity().getExternalId(); 
				String secondaryEntityExtId = ((UserPrivilegesResourceEntitlement) userResEntmt).getSecondaryEntity().getExternalId(); 	
				userResRejectEvent.getEntitlements().add(new Entitlement(primaryEntityExtId, secondaryEntityExtId));
			}
		}
		send(new Event<EntitlementsRejectedEvent>(Events.ENTITLEMENTS_REJECTED_EVENT, userResRejectEvent, jsonMapper));
	}
}