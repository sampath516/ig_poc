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

import com.poc.ig.repo.dto.CreateResourceRequest;
import com.poc.ig.repo.dto.CreateResourceResponse;
import com.poc.ig.repo.dto.GetResourceResponse;
import com.poc.ig.repo.dto.ListResourcesResponse;
import com.poc.ig.repo.dto.UpdateResourceRequest;
import com.poc.ig.repo.dto.UpdateResourceResponse;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.exception.InvalidOrganizationIdException;
import com.poc.ig.repo.exception.InvalidResourceIdException;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.ResourceRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/{tenantId}/", produces = "application/json", consumes = "application/json")
public class ResourceService {
	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private ResourceRepository resourceRepo;

	@PostMapping(path = "{orgId}/resources")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateResourceResponse createResource(@PathVariable long tenantId, @PathVariable long orgId, @RequestBody CreateResourceRequest createResourceReq) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		Resource resource = createResourceReq.getResource();
		resource.setOrganization(org);
		return new CreateResourceResponse(resourceRepo.save(resource));
	}

	@GetMapping(path = "{orgId}/resources/{resourceId}")
	public ResponseEntity<GetResourceResponse> getResource(@PathVariable long tenantId, @PathVariable long orgId, @PathVariable long resourceId) {
		Resource resource = validateResource(tenantId, orgId, resourceId);
		return new ResponseEntity<>(new GetResourceResponse(resource), HttpStatus.OK);
	}

	@GetMapping(path = "{orgId}/resources")
	@ResponseStatus(HttpStatus.OK)
	public ListResourcesResponse listResources(@PathVariable long tenantId, @PathVariable long orgId) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		return new ListResourcesResponse(org.getResources());
	}

	@PutMapping(path = "{orgId}/resources")
	public ResponseEntity<UpdateResourceResponse> updateResource(@PathVariable long tenantId, @PathVariable long orgId, @RequestBody UpdateResourceRequest updateResourceReq,
			@RequestParam boolean basicUpdate) {
		Resource resource = validateResource(tenantId, orgId, updateResourceReq.getId());
		resource = updateResourceReq.getUpdatedRole(resource, basicUpdate);
		return new ResponseEntity<>(new UpdateResourceResponse(resourceRepo.save(resource)), HttpStatus.OK);
	}

	@DeleteMapping(path = "{orgId}/resources/{resourceId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteResource(@PathVariable long tenantId, @PathVariable long orgId, @PathVariable long resourceId) {
		resourceRepo.delete(validateResource(tenantId, orgId, resourceId));
	}

	private Resource validateResource(long tenantId, long orgId, long resourceId) {
		List<Long> resourceIds = new ArrayList<>();
		resourceIds.add(resourceId);
		return validateResources(tenantId, orgId, resourceIds).get(0);
	}

	private List<Resource> validateResources(long tenantId, long orgId, List<Long> resourceIds) {
		validateTenantOrganization(tenantId, orgId);
		List<Resource> resources = new ArrayList<>();
		for (long resourceId : resourceIds) {
			Resource resource = resourceRepo.findByOrgIdAndResourceId(orgId, resourceId);
			if (resource == null) {
				throw new InvalidResourceIdException("Resource does not exist: " + resourceId);
			}
			resources.add(resource);
		}
		return resources;
	}

	private Organization validateTenantOrganization(long tenantId, long orgId) {
		Organization org = orgRepo.findByTenantIdAndOrgId(tenantId, orgId);
		if (org != null) {
			return org;
		} else {
			throw new InvalidOrganizationIdException(
					"Organization Id, " + orgId + ", does not belong to the tenant Id " + tenantId);
		}
	}
}
