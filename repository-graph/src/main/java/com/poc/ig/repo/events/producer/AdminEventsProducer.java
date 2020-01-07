package com.poc.ig.repo.events.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Tenant;
import com.poc.ig.repo.events.Events;
import com.poc.ig.repo.events.KafkaTopics;
import com.poc.ig.repo.events.dto.Event;
import com.poc.ig.repo.events.dto.OrganizationDto;
import com.poc.ig.repo.events.dto.TenantDto;

@Component
public class AdminEventsProducer {
	@SuppressWarnings("rawtypes")
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;
	
	@Autowired
	private ObjectMapper jsonObjectMapper;

	@SuppressWarnings("rawtypes")
	public void send(Event event) {
		kafkaTemplate.send(KafkaTopics.ADMIN_EVENT_TOPIC, event);
	}
	
	
	public Event<TenantDto> prepareNewTenantEvent(Tenant tenant ) {
		TenantDto tenantDto = new TenantDto(tenant);
		return new Event<TenantDto>(Events.NEW_TENANT_EVENT, tenantDto, jsonObjectMapper );
	}
	
	public Event<OrganizationDto> prepareNewOrganizationEvent(Organization org) {
		OrganizationDto orgDto = new OrganizationDto(org);
		return new Event<OrganizationDto>(Events.NEW_ORGANIZATION_EVENT, orgDto, jsonObjectMapper);
	}
}
