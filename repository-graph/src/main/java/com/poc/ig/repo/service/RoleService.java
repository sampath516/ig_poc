package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.repo.dto.CreateRoleRequest;
import com.poc.ig.repo.dto.CreateRoleResponse;
import com.poc.ig.repo.dto.DeleteRolesRequest;
import com.poc.ig.repo.dto.GetRoleResponse;
import com.poc.ig.repo.dto.LinkRoleResourcesResponse;
import com.poc.ig.repo.dto.ListRolesResponse;
import com.poc.ig.repo.dto.RoleDto;
import com.poc.ig.repo.dto.UnlinkRoleResourcesResponse;
import com.poc.ig.repo.dto.UpdateRoleRequest;
import com.poc.ig.repo.dto.UpdateRoleResponse;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Role;
import com.poc.ig.repo.entity.Tenant;
import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.exception.InvalidOrganizationException;
import com.poc.ig.repo.exception.InvalidResourceException;
import com.poc.ig.repo.exception.InvalidRoleException;
import com.poc.ig.repo.exception.InvalidTenantException;
import com.poc.ig.repo.exception.InvalidUserException;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.ResourceRepository;
import com.poc.ig.repo.repository.RoleRepository;
import com.poc.ig.repo.repository.TenantRepository;
import com.poc.ig.repo.repository.UserRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/tenants/{tenantName}/", produces = "application/json", consumes = "application/json")
public class RoleService {
	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private ResourceRepository resourceRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TenantRepository tenantRepo;
	
	@PostMapping(path = "roles")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateRoleResponse createRole(@PathVariable String tenantName, @RequestBody CreateRoleRequest createRoleReq) {
		
		List<Role> roleEntities = new ArrayList<Role>();
		for (RoleDto dto : createRoleReq.getRoles()) {
			Role r = dto.getRole();
			Organization org = validateOrganization(tenantName, dto.getOrganization());
			r.setTenant(org.getTenant());
			r.setOrganization(org);
			org.getRoles().add(r);
			User owner = validateUser(tenantName, dto.getOwner());
			r.setOwner(owner);
			r = roleRepo.save(r);
			roleEntities.add(r);
		}
		return new CreateRoleResponse(roleEntities);
	}

	@GetMapping(path = "roles/{roleExternalId}")
	public ResponseEntity<GetRoleResponse> getRole(@PathVariable String tenantName, @PathVariable String roleExternalId) {
		Role role = validateRole(tenantName, roleExternalId);
		return new ResponseEntity<>(new GetRoleResponse(role), HttpStatus.OK);
	}

	@GetMapping(path = "roles")
	@ResponseStatus(HttpStatus.OK)
	public ListRolesResponse listRoles(@PathVariable String tenantName) {
		validateTenant(tenantName);
		return new ListRolesResponse(roleRepo.findByTenantName(tenantName));
	}

	@PutMapping(path = "roles/{roleExternalId}")
	public ResponseEntity<UpdateRoleResponse> updateRole(@PathVariable String tenantName, @PathVariable String roleExternalId, @RequestBody UpdateRoleRequest updateRoleReq) {
		Role role = validateRole(tenantName, roleExternalId);
		role.setName(updateRoleReq.getName());
		role.setDescription(updateRoleReq.getDescription());
		if(!role.getOrganization().getExternalId().equals(updateRoleReq.getOrganization())) {
			Organization org = validateOrganization(tenantName, updateRoleReq.getOrganization());
			role.setOrganization(org);
		}
		if(!role.getOwner().getExternalId().equals(updateRoleReq.getOwner())) {
			User user = validateUser(tenantName, updateRoleReq.getOwner());
			role.setOwner(user);
		}
		return new ResponseEntity<>(new UpdateRoleResponse(roleRepo.save(role)), HttpStatus.OK);
	}

	@DeleteMapping(path = "roles/{roleExternalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable String tenantName, @PathVariable String roleExternalId) {
		roleRepo.delete(validateRole(tenantName, roleExternalId ));
	}
	
	@DeleteMapping(path = "roles")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRoles(@PathVariable String tenantName, @RequestBody DeleteRolesRequest deleteRolesRequest) {
		for(String roleExtId : deleteRolesRequest.getRoles()) {
			roleRepo.delete(validateRole(tenantName, roleExtId ));
		}		
	}

	@PutMapping(path = "roles/{roleExternalId}/resources")
	public ResponseEntity<LinkRoleResourcesResponse> linkRoleResources(@PathVariable String tenantName, @PathVariable String roleExternalId, List<String> resources) {
		Role role = validateRole(tenantName, roleExternalId);
		for(String res : resources) {
			Resource resEntity = validateResource(tenantName, res);
			role.getResources().add(resEntity);
			resEntity.getRoles().add(role);
			resourceRepo.save(resEntity);
		}
		return new ResponseEntity<>(new LinkRoleResourcesResponse(roleRepo.save(role)), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "{orgId}/roles/{roleId}/resources")
	public ResponseEntity<UnlinkRoleResourcesResponse> unlinkRoleResources(@PathVariable String tenantName, @PathVariable String roleExternalId, List<String> resources) {
		Role role = validateRole(tenantName, roleExternalId);
		for(String res : resources) {
			Resource resEntity = validateResource(tenantName, res);
			role.getResources().remove(resEntity);
			resEntity.getRoles().remove(role);
			resourceRepo.save(resEntity);
		}
		return new ResponseEntity<>(new UnlinkRoleResourcesResponse(roleRepo.save(role)), HttpStatus.OK);
	}

	private User validateUser(String tenantName, String userExternalId) {
		Optional<User> user = userRepo.findByTenantNameAndUserExternalId(tenantName, userExternalId);
		if (user.isPresent()) {
			return user.get(); 
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName+", "+userExternalId+")");
		}
	}
	
	private Tenant validateTenant(String tenantName) {
		Optional<Tenant> tenant = tenantRepo.findByName(tenantName);
		if (tenant.isPresent()) {
			return tenant.get(); 
		} else {
			throw new InvalidTenantException("Invalid Tenant: " + tenantName);
		}
		
	}
	
	private Role validateRole(String tenantName, String roleExternalId) {
		Optional<Role> role = roleRepo.findByTenantNameAndRoleExternalId(tenantName, roleExternalId);		
		if (role.isPresent()) {
			return role.get(); 
		} else {
			throw new InvalidRoleException("Invalid Role(" + tenantName+", "+roleExternalId+")");
		}
	}
	
	private Resource validateResource(String tenantName, String resourceExternalId) {
		Optional<Resource> resource = resourceRepo.findByTenantNameAndResourceExternalId(tenantName, resourceExternalId);
		if (resource.isPresent()) {
			return resource.get(); 
		} else {
			throw new InvalidResourceException("Invalid Resource(" + tenantName+", "+resourceExternalId+")");
		}
	}
	
	private Organization validateOrganization(String tenantName, String orgExternalId) {
		Optional<Organization> org = orgRepo.findByTenantNameAndOrgExternalId(tenantName, orgExternalId);

		if (org.isPresent()) {
			return org.get();
		} else {
			throw new InvalidOrganizationException("Invalid Organizatio(" + tenantName + ", " + orgExternalId + ")");
		}
	}

}
