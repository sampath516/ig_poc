package com.poc.ig.certification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.certification.dto.CreateResourceRequest;
import com.poc.ig.certification.dto.CreateResourceResponse;
import com.poc.ig.certification.dto.ResourceRequest;
import com.poc.ig.certification.dto.ResourceResponse;
import com.poc.ig.certification.entity.Application;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Resource;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.exception.InvalidApplicationException;
import com.poc.ig.certification.exception.InvalidResourceException;
import com.poc.ig.certification.exception.InvalidUserException;
import com.poc.ig.certification.repository.ApplicationRepository;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.repository.ResourceRepository;
import com.poc.ig.certification.repository.UserRepository;


@RestController
@RequestMapping(value = "ig/certification/v1/tenants/{tenantName}/{certificationName}", produces = "application/json", consumes = "application/json")
public class ResourceService {
	@Autowired
	private ResourceRepository resourceRepo;
	
	@Autowired
	private ApplicationRepository appRepo;
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CertificationRepository certRepo;

	@PostMapping(path = "resources")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateResourceResponse createResource(@PathVariable String tenantName, @PathVariable String certificationName, @RequestBody CreateResourceRequest createResourceReq) {	
		Certification cert = ServicesUtil.validateCertification(certRepo, tenantName, certificationName);
		List<ResourceRequest> resourcesIn = createResourceReq.getResources();
		List<Resource> resourceEntities = new ArrayList<Resource>();
		for(ResourceRequest resIn : resourcesIn) {
			User owner = validateUser(tenantName, resIn.getOwner());
			Application app = validateApplication(tenantName, resIn.getApplication());			
			
			Resource resource = new Resource();
			
			resource.setExternalId(resIn.getExternalId());
			resource.setName(resIn.getName());
			resource.setDescription(resIn.getDescription());
			
			resource.setOwner(owner);
			owner.getOwnedResources().add(resource);
			
			resource.setApplication(app);
			app.getResources().add(resource);

			resource.setTenant(app.getTenant());
			resource.setTenantName(tenantName);
			
			resource.setCertification(cert);
			resource.setCertificationName(cert.getName());
			
			resource = resourceRepo.save(resource);
			resourceEntities.add(resource);
		}		
			
		return new CreateResourceResponse(resourceEntities);
	}

	@GetMapping(path = "resources/{resourceExternalId}")
	public ResponseEntity<ResourceResponse> getResource(@PathVariable String tenantName, @PathVariable String certificationName, @PathVariable String resourceExternalId) {
		Resource resource = validateResource(tenantName, resourceExternalId);		
		Optional<Resource> resourceTemp = resourceRepo.findById(resource.getKey());
		if (resourceTemp.isPresent()) {
			resource = resourceTemp.get(); 
		} else {
			throw new InvalidResourceException("Invalid Resource(" + tenantName+", "+resourceExternalId+")");
		}			
		return new ResponseEntity<>(new ResourceResponse(resource), HttpStatus.OK);
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

	private User validateUser(String tenantName, String userExternalId) {
		Optional<User> user = userRepo.findByTenantNameAndUserExternalId(tenantName, userExternalId);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
	}
}
