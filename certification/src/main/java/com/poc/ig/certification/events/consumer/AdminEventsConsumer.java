package com.poc.ig.certification.events.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.certification.entity.Organization;
import com.poc.ig.certification.entity.Tenant;
import com.poc.ig.certification.events.Events;
import com.poc.ig.certification.events.KafkaTopics;
import com.poc.ig.certification.events.dto.Event;
import com.poc.ig.certification.events.dto.OrganizationDto;
import com.poc.ig.certification.events.dto.TenantDto;
import com.poc.ig.certification.repository.OrganizationRepository;
import com.poc.ig.certification.repository.TenantRepository;
import com.poc.ig.certification.service.ServicesUtil;

@Component
public class AdminEventsConsumer {
	
	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	ObjectMapper jsonObjectMapper;
	
	@KafkaListener(containerFactory = "kafkaEventsListenerContainerFactory", topics = KafkaTopics.ADMIN_EVENT_TOPIC)
	public void listen(@SuppressWarnings("rawtypes") Event event) {
		
		if (event.getName().equals(Events.NEW_TENANT_EVENT)) {
			TenantDto tenantDto = (TenantDto) event.getEventData(jsonObjectMapper, TenantDto.class);  
			tenantRepo.save(tenantDto.tenant());
		}
		
		if (event.getName().equals(Events.NEW_ORGANIZATION_EVENT)) {
			OrganizationDto orgDto = (OrganizationDto) event.getEventData(jsonObjectMapper, OrganizationDto.class);
			Tenant tenant = ServicesUtil.validateTenant(tenantRepo, orgDto.getTenantName());
			Organization org = orgDto.organization();
			org.setTenant(tenant);
			org.setTenantName(tenant.getName());
			orgRepo.save(org);
		}
	}

}
