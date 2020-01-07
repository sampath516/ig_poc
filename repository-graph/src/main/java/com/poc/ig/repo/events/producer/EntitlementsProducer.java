package com.poc.ig.repo.events.producer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.poc.ig.repo.dto.UserResourceEntitlement;
import com.poc.ig.repo.dto.UserResourceEntitlement.ResourceDto;
import com.poc.ig.repo.dto.UserResourceEntitlement.UserDto;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.events.KafkaTopics;
import com.poc.ig.repo.events.dto.Event;
import com.poc.ig.repo.repository.UserRepository;

@Component
public class EntitlementsProducer {

	@SuppressWarnings("rawtypes")
	@Autowired
	KafkaTemplate<String, Event> kafkaTemplate;

	@Autowired
	private UserRepository userRepo;

	@SuppressWarnings("rawtypes")
	public void send(Event event) {
		kafkaTemplate.send(KafkaTopics.ENTITLEMENTS_EVENT_TOPIC, event);
	}

	public List<UserResourceEntitlement> getUserResourceEntitlementsEvent(String tenantName, String organizationName) {
		List<UserResourceEntitlement> entitlements = new ArrayList<UserResourceEntitlement>();

		List<User> users = userRepo.findAllUserResourceEntitlementsByTenantNameAndOrganizationName(tenantName,
				organizationName);

		for (User u : users) {
			String manager = u.getManager() == null ? null : u.getManager().getExternalId();
			UserDto primaryEntity = new UserDto(u.getExternalId(), u.getUserName(), u.getFirstName(), u.getLastName(),
					u.getEmail(), manager);

			for (Resource r : u.getResources()) {
				String owner = r.getOwner() == null ? null : r.getOwner().getExternalId();
				ResourceDto secondaryEntity = new ResourceDto(r.getExternalId(), r.getName(), r.getDescription(), r.getApplication().getExternalId(), owner);
				UserResourceEntitlement entmt = new UserResourceEntitlement();
				entmt.setTenantName(tenantName);
				entmt.setOrganization(organizationName);
				entmt.setPrimaryEntity(primaryEntity);
				entmt.setSecondaryEntity(secondaryEntity);
				entitlements.add(entmt);
			}
		}
		return entitlements;
	}
}
