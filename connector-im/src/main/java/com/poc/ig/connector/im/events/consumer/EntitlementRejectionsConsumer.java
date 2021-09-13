package com.poc.ig.connector.im.events.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.connector.im.events.Events;
import com.poc.ig.connector.im.events.KafkaTopics;
import com.poc.ig.connector.im.events.dto.EntitlementsRejectedEvent;
import com.poc.ig.connector.im.events.dto.EntitlementsRejectedEvent.Entitlement;
import com.poc.ig.connector.im.events.dto.EntitlementsRejectedEvent.EntitlementType;
import com.poc.ig.connector.im.events.dto.Event;
import com.poc.ig.connector.im.exportdata.IMExportService;


@Component
public class EntitlementRejectionsConsumer {
	
	@Autowired
	ObjectMapper jsonObjectMapper;
	
	@Autowired
	IMExportService imExportService;

	@KafkaListener(containerFactory = "kafkaEventsListenerContainerFactory", topics = KafkaTopics.ENTITLEMENT_REJECTION_EVENT_TOPIC)
	public void listen(Event<EntitlementsRejectedEvent> event) throws Exception {
		if (event.getName().equals(Events.ENTITLEMENTS_REJECTED_EVENT)) {
			EntitlementsRejectedEvent entRejEvent = event.getEventData(jsonObjectMapper, EntitlementsRejectedEvent.class);
			
			if(entRejEvent.getEntitlementType().equals(EntitlementType.USER_RESOURCE)) {
				rejectUserResourceEntitlements(entRejEvent.getTenant(),entRejEvent.getCertificationName(), entRejEvent.getEntitlements());
			}
		}
	}
		
	private void rejectUserResourceEntitlements(String tenantName, String certificationName,  List<Entitlement> entitlements) throws Exception {		
		imExportService.exportUserResourceEntitlementRejections(tenantName, certificationName, entitlements);
	}
}
