package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.repo.dto.CreateRoleRequest;
import com.poc.ig.repo.dto.CreateRoleResponse;
import com.poc.ig.repo.dto.GetRoleResponse;
import com.poc.ig.repo.dto.LinkRoleResourcesResponse;
import com.poc.ig.repo.dto.ListRolesResponse;
import com.poc.ig.repo.dto.UnlinkRoleResourcesResponse;
import com.poc.ig.repo.dto.UpdateRoleRequest;
import com.poc.ig.repo.dto.UpdateRoleResponse;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Role;
import com.poc.ig.repo.exception.InvalidOrganizationIdException;
import com.poc.ig.repo.exception.InvalidResourceIdException;
import com.poc.ig.repo.exception.InvalidRoleIdException;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.ResourceRepository;
import com.poc.ig.repo.repository.RoleRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/{tenantId}/", produces = "application/json", consumes = "application/json")
public class RoleService {
	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private ResourceRepository resourceRepo;

	@PostMapping(path = "{orgId}/roles")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateRoleResponse createRole(@PathVariable String tenantId, @PathVariable String orgId,
			@RequestBody CreateRoleRequest createRoleReq) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		Role role = createRoleReq.getRole();
		role.setOrganization(org);
		return new CreateRoleResponse(roleRepo.save(role));
	}

	@GetMapping(path = "{orgId}/roles/{roleId}")
	public ResponseEntity<GetRoleResponse> getRole(@PathVariable String tenantId, @PathVariable String orgId,
			@PathVariable String roleId) {
		Role role = validateRole(tenantId, orgId, roleId);
		return new ResponseEntity<>(new GetRoleResponse(role), HttpStatus.OK);
	}

	@GetMapping(path = "{orgId}/roles")
	@ResponseStatus(HttpStatus.OK)
	public ListRolesResponse listRoles(@PathVariable String tenantId, @PathVariable String orgId) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		return new ListRolesResponse(org.getRoles());
	}

	@PutMapping(path = "{orgId}/roles")
	public ResponseEntity<UpdateRoleResponse> updateRole(@PathVariable String tenantId, @PathVariable String orgId,
			@RequestBody UpdateRoleRequest updateRoleReq, @RequestParam boolean basicUpdate) {
		Role role = validateRole(tenantId, orgId, updateRoleReq.getId());
		role = updateRoleReq.getUpdatedRole(role, basicUpdate);
		return new ResponseEntity<>(new UpdateRoleResponse(roleRepo.save(role)), HttpStatus.OK);
	}

	@DeleteMapping(path = "{orgId}/roles/{roleId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable String tenantId, @PathVariable String orgId, @PathVariable String roleId) {
		roleRepo.delete(validateRole(tenantId, orgId, roleId));
	}

	@PutMapping(path = "{orgId}/roles/{roleId}/resources")
	public ResponseEntity<LinkRoleResourcesResponse> linkRoleResources(@PathVariable String tenantId,
			@PathVariable String orgId, @PathVariable String roleId, List<String> resourceIds) {
		Role role = validateRole(tenantId, orgId, roleId);
		List<Resource> resources = validateResources(tenantId, orgId, resourceIds);
		for (Resource r : resources) {
			role.getResources().add(r);
			r.getRoles().add(role);
			resourceRepo.save(r);
		}
		return new ResponseEntity<>(new LinkRoleResourcesResponse(roleRepo.save(role)), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "{orgId}/roles/{roleId}/resources")
	public ResponseEntity<UnlinkRoleResourcesResponse> unlinkRoleResources(@PathVariable String tenantId,
			@PathVariable String orgId, @PathVariable String roleId, List<String> resourceIds) {
		Role role = validateRole(tenantId, orgId, roleId);
		List<Resource> resources = validateResources(tenantId, orgId, resourceIds);
		for (Resource r : resources) {
			role.getResources().remove(r);
			r.getRoles().remove(role);
			resourceRepo.save(r);
		}
		return new ResponseEntity<>(new UnlinkRoleResourcesResponse(roleRepo.save(role)), HttpStatus.OK);
	}
	
	private List<Resource> validateResources(String tenantId, String orgId, List<String> resourceIds) {
		validateTenantOrganization(tenantId, orgId);
		List<Resource> resources = new ArrayList<>();
		for (String resourceId : resourceIds) {
			Resource resource = resourceRepo.findByOrgIdAndResourceId(orgId, resourceId);
			if (resource == null) {
				throw new InvalidResourceIdException("Resource does not exist: " + resourceId);
			}
			resources.add(resource);
		}
		return resources;
	}
	
	
	
	private Role validateRole(String tenantId, String orgId, String roleId) {
		List<String> roleIds = new ArrayList<>();
		roleIds.add(roleId);
		List<Role> roles = validateRoles(tenantId, orgId, roleIds);
		return roles.get(0);
	}

	private List<Role> validateRoles(String tenantId, String orgId, List<String> roleIds) {
		validateTenantOrganization(tenantId, orgId);
		List<Role> roles = new ArrayList<>();
		for (String roleId : roleIds) {
			Role role = roleRepo.findByOrgIdAndRoleId(orgId, roleId);
			if (role == null) {
				throw new InvalidRoleIdException("Role does not exist: " + roleId);
			}
			roles.add(role);
		}
		return roles;
	}

	private Organization validateTenantOrganization(String tenantId, String orgId) {
		Organization org = orgRepo.findByTenantIdAndOrgId(tenantId, orgId);
		if (org != null) {
			return org;
		} else {
			throw new InvalidOrganizationIdException(
					"Organization Id, " + orgId + ", does not belong to the tenant Id " + tenantId);
		}
	}
}
