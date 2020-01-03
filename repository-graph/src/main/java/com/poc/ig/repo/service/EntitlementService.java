package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.repo.dto.UserResourceEntitlement;
import com.poc.ig.repo.dto.UserResourceEntitlement.ResourceDto;
import com.poc.ig.repo.dto.UserResourceEntitlement.UserDto;
import com.poc.ig.repo.dto.UserResourceEntitlementResponse;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.repository.UserRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/tenants/{tenantName}/organizations/{organizationName}/", produces = "application/json", consumes = "application/json")
public class EntitlementService {

	@Autowired
	private UserRepository userRepo;

	@GetMapping(path = "users/resources")
	public  ResponseEntity<UserResourceEntitlementResponse> getUserResourceEntitlements(@PathVariable String tenantName, @PathVariable String organizationName) {
		List<UserResourceEntitlement> entitlements = new ArrayList<UserResourceEntitlement>();
		
		List<User> users = userRepo.findAllUserResourceEntitlementsByTenantNameAndOrganizationName(tenantName, organizationName);
		
		for(User u:users) {
			String manager = u.getManager() == null ? null : u.getManager().getExternalId();
			UserDto primaryEntity = new UserDto(u.getExternalId(), u.getUserName(), u.getFirstName(), u.getLastName(), u.getEmail(), manager);
		
			for(Resource r: u.getResources()) {
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
		return new ResponseEntity<>(new UserResourceEntitlementResponse(entitlements), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "users/resources")
	public void deleteUserResourceEntitlement() {
		
	}

}
