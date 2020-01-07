package com.poc.ig.certification.events.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Certification.CertificationState;
import com.poc.ig.certification.events.Events;
import com.poc.ig.certification.events.KafkaTopics;
import com.poc.ig.certification.events.dto.EntitlementsPublishedEventDto;
import com.poc.ig.certification.events.dto.Event;
import com.poc.ig.certification.exception.EntitlementsPublishedEventException;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.service.ServicesUtil;

@Component
public class RepositoryEventsConsumer {
	
	@Autowired
	ObjectMapper jsonObjectMapper;
	
	@Autowired
	CertificationRepository certRepo;
	
	@KafkaListener(containerFactory = "kafkaEventsListenerContainerFactory", topics = KafkaTopics.REPOSITORY_EVENT_TOPIC)
	public void listen(@SuppressWarnings("rawtypes") Event event) {
		
		if (event.getName().equals(Events.ENTITLEMENTS_PUBLISHED_EVENT)) {
			EntitlementsPublishedEventDto entitlementsPublishedEvent = (EntitlementsPublishedEventDto) event.getEventData(jsonObjectMapper, EntitlementsPublishedEventDto.class); 
			Certification cert = ServicesUtil.findCertificationById(certRepo, entitlementsPublishedEvent.getTenantName(), entitlementsPublishedEvent.getCertificationName(), 1);
			
			int sleepInterval = 10000;
			int maxWaitIterations = 180; 
			int i = 0;
			while(i < maxWaitIterations) {
				if(cert.getEntitlements().size() < entitlementsPublishedEvent.getEntitlementsCount()  ) {
					try {
						Thread.sleep(sleepInterval);
						cert = ServicesUtil.findCertificationById(certRepo, entitlementsPublishedEvent.getTenantName(), entitlementsPublishedEvent.getCertificationName(), 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
						throw new EntitlementsPublishedEventException(e);						
					}
				}else {
					cert.setState(CertificationState.IN_PROGRESS);
					certRepo.save(cert);
					break;
				}
				i++;
			}
			
		}
	}
}
