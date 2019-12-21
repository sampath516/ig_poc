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

import com.poc.ig.repo.dto.CreateResourceRequest;
import com.poc.ig.repo.dto.CreateResourceResponse;
import com.poc.ig.repo.dto.DeleteResourcesRequest;
import com.poc.ig.repo.dto.GetResourceResponse;
import com.poc.ig.repo.dto.ListResourcesResponse;
import com.poc.ig.repo.dto.ResourceRequest;
import com.poc.ig.repo.dto.UpdateResourceRequest;
import com.poc.ig.repo.dto.UpdateResourceResponse;
import com.poc.ig.repo.entity.Application;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Tenant;
import com.poc.ig.repo.exception.InvalidApplicationException;
import com.poc.ig.repo.exception.InvalidResourceException;
import com.poc.ig.repo.exception.InvalidTenantException;
import com.poc.ig.repo.repository.ApplicationRepository;
import com.poc.ig.repo.repository.ResourceRepository;
import com.poc.ig.repo.repository.TenantRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/tenants/{tenantName}/", produces = "application/json", consumes = "application/json")
public class ResourceService {
	@Autowired
	private ResourceRepository resourceRepo;
	
	@Autowired
	private ApplicationRepository appRepo;
	
	@Autowired
	private TenantRepository tenantRepo;

	@PostMapping(path = "resources")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateResourceResponse createResource(@PathVariable String tenantName, @RequestBody CreateResourceRequest createResourceReq) {	
		List<ResourceRequest> resourcesIn = createResourceReq.getResources();
		List<Resource> resourceEntities = new ArrayList<Resource>();
		for(ResourceRequest resIn : resourcesIn) {
			Application app = validateApplication(tenantName, resIn.getApplication());			
			Resource resource = new Resource();
			resource.setExternalId(resIn.getExternalId());
			resource.setApplication(app);
			app.getResources().add(resource);
			resource.setName(resIn.getName());
			resource.setDescription(resIn.getDescription());
			resource.setTenant(app.getTenant());
			resource = resourceRepo.save(resource);
			resourceEntities.add(resource);
		}		

		return new CreateResourceResponse(resourceEntities);
	}

	@GetMapping(path = "resources/{resourceExternalId}")
	public ResponseEntity<GetResourceResponse> getResource(@PathVariable String tenantName,@PathVariable String resourceExternalId) {
		Resource resource = validateResource(tenantName, resourceExternalId);
		return new ResponseEntity<>(new GetResourceResponse(resource), HttpStatus.OK);
	}

	@GetMapping(path = "resources")
	@ResponseStatus(HttpStatus.OK)
	public ListResourcesResponse listResources(@PathVariable String tenantName) {
		validateTenant(tenantName);
		return new ListResourcesResponse(resourceRepo.findByTenantName(tenantName));
	}

	@PutMapping(path = "resources/{resourceExternalId}")
	public ResponseEntity<UpdateResourceResponse> updateResource(@PathVariable String tenantName, @PathVariable String resourceExternalId, @RequestBody UpdateResourceRequest updateResourceReq) {
		Resource resource = validateResource(tenantName, resourceExternalId);
		resource.setName(updateResourceReq.getName());
		resource.setDescription(updateResourceReq.getDescription());
		return new ResponseEntity<>(new UpdateResourceResponse(resourceRepo.save(resource)), HttpStatus.OK);
	}

	@DeleteMapping(path = "resources/{resourceExternalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteResource(@PathVariable String tenantName, @PathVariable String resourceExternalId) {
		resourceRepo.delete(validateResource(tenantName, resourceExternalId));
	}
	
	@DeleteMapping(path = "resources")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteResources(@PathVariable String tenantName, @RequestBody DeleteResourcesRequest deleteResourcesReq) {
		for(String resExtId: deleteResourcesReq.getResources()) {
			resourceRepo.delete(validateResource(tenantName, resExtId));
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
	
	private Application validateApplication(String tenantName, String appExternalId) {
		Optional<Application> app = appRepo.findByTenantNameAndAppExternalId(tenantName, appExternalId);
		if (app.isPresent()) {
			return app.get(); 
		} else {
			throw new InvalidApplicationException("Invalid Application(" + tenantName+", "+appExternalId+")");
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
}
