package com.poc.ig.certification.events.producer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.certification.entity.Entitlement.EntitlementType;
import com.poc.ig.certification.entity.Resource;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;
import com.poc.ig.certification.events.Events;
import com.poc.ig.certification.events.KafkaTopics;
import com.poc.ig.certification.events.dto.EntitlementsRejectedEvent;
import com.poc.ig.certification.events.dto.EntitlementsRejectedEvent.Entitlement;
import com.poc.ig.certification.events.dto.EntitlementsRejectedEvent.PrimaryEntity;
import com.poc.ig.certification.events.dto.EntitlementsRejectedEvent.SecondaryEntity;
import com.poc.ig.certification.events.dto.Event;

@Component
public class EntitlementsRejectionProducer {
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;
	
	public void send(Event<EntitlementsRejectedEvent> event) {
		kafkaTemplate.send(KafkaTopics.ENTITLEMENT_REJECTION_EVENT_TOPIC, event);
	}
	
	
	public void sendEvents(String tenantName, List<com.poc.ig.certification.entity.Entitlement> entitlements, ObjectMapper jsonMapper, String certificationName) {
		EntitlementsRejectedEvent userResRejectEvent = new EntitlementsRejectedEvent();
		userResRejectEvent.setTenant(tenantName);
		userResRejectEvent.setCertificationName(certificationName);
		userResRejectEvent.setEntitlementType(EntitlementType.USER_RESOURCE);
		for(com.poc.ig.certification.entity.Entitlement userResEntmt: entitlements) {
			if(userResEntmt instanceof UserPrivilegesResourceEntitlement) {
				
			User user = ((UserPrivilegesResourceEntitlement) userResEntmt).getPrimaryEntity();
			Resource resource = ((UserPrivilegesResourceEntitlement) userResEntmt).getSecondaryEntity();

				PrimaryEntity primary = new PrimaryEntity(user.getExternalId());
				SecondaryEntity secondary = new SecondaryEntity(resource.getExternalId());
				secondary.getProperties().put("name", resource.getName());
				secondary.getProperties().put("name2", resource.getName2());
				secondary.getProperties().put("name3", resource.getName3());
				secondary.getProperties().put("type", resource.getType());
				userResRejectEvent.getEntitlements().add(new Entitlement(primary, secondary));
			}
		}
		send(new Event<EntitlementsRejectedEvent>(Events.ENTITLEMENTS_REJECTED_EVENT, userResRejectEvent, jsonMapper));
	}
}