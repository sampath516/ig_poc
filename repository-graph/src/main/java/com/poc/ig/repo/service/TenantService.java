package com.poc.ig.repo.service;

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

import com.poc.ig.repo.dto.CreateApplicationRequest;
import com.poc.ig.repo.dto.CreateApplicationResponse;
import com.poc.ig.repo.dto.CreateOrganizationRequest;
import com.poc.ig.repo.dto.CreateOrganizationResponse;
import com.poc.ig.repo.dto.CreateTenantRequest;
import com.poc.ig.repo.dto.CreateTenantResponse;
import com.poc.ig.repo.dto.ApplicationResponse;
import com.poc.ig.repo.dto.GetTenantResponse;
import com.poc.ig.repo.dto.ListOrganizationsResponse;
import com.poc.ig.repo.dto.ListTenantsResponse;
import com.poc.ig.repo.dto.OrganizationResponse;
import com.poc.ig.repo.dto.UpdateTenantRequest;
import com.poc.ig.repo.dto.UpdateTenantResponse;
import com.poc.ig.repo.entity.Application;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Tenant;
import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.exception.InvalidApplicationException;
import com.poc.ig.repo.exception.InvalidOrganizationException;
import com.poc.ig.repo.exception.InvalidTenantException;
import com.poc.ig.repo.exception.InvalidUserException;
import com.poc.ig.repo.repository.ApplicationRepository;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.TenantRepository;
import com.poc.ig.repo.repository.UserRepository;


@RestController
@RequestMapping(value = "ig/repo/v1/", produces = "application/json", consumes = "application/json")
public class TenantService {

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private OrganizationRepository orgRepo;
	
	@Autowired
	private ApplicationRepository appRepo;
	
	@Autowired
	private UserRepository userRepo;

	@PostMapping(path = "tenants")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateTenantResponse createTenant(@RequestBody CreateTenantRequest createTenantReq) {
		return new CreateTenantResponse(tenantRepo.save(createTenantReq.tenant()));
	}

	@GetMapping(path = "tenants/{tenantName}")
	public ResponseEntity<GetTenantResponse> getTenant(@PathVariable String tenantName) {
		Tenant tenant = validateTenant(tenantName);
		return new ResponseEntity<>(new GetTenantResponse(tenant), HttpStatus.OK);

	}
	
	@PutMapping(path = "tenants/{tenantName}")
	@ResponseStatus(HttpStatus.OK)
	public UpdateTenantResponse updateTenant(@PathVariable String tenantName, @RequestBody UpdateTenantRequest updateTenantReq) {
		Tenant tenant = validateTenant(tenantName);
		tenant.setDescription(updateTenantReq.getDescription());
		return new UpdateTenantResponse(tenantRepo.save(tenant));
	}
	
	@GetMapping(path = "tenants")
	@ResponseStatus(HttpStatus.OK)
	public ListTenantsResponse listTenants() {
		return new ListTenantsResponse(tenantRepo.findAll());
	}

	@DeleteMapping(path = "tenants/{tenantName}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTenant(@PathVariable String tenantName) {
		tenantRepo.delete(validateTenant(tenantName));
	}

	@GetMapping(path = "tenants/{tenantName}/organizations")
	@ResponseStatus(HttpStatus.OK)
	public ListOrganizationsResponse listOrganizations(@PathVariable String tenantName) {
		validateTenant(tenantName);
		return new ListOrganizationsResponse(orgRepo.findByTenantName(tenantName));
	}

	@PostMapping(path = "tenants/{tenantName}/organizations")
	public ResponseEntity<CreateOrganizationResponse> createOrganization(@PathVariable String tenantName,	@RequestBody CreateOrganizationRequest createOrganizationReq) {
		Tenant tenant = validateTenant(tenantName);
		Organization org = createOrganizationReq.getOrganization();
		org.setTenant(tenant);
		return new ResponseEntity<>(new CreateOrganizationResponse(orgRepo.save(org)), HttpStatus.CREATED);
	}

	@DeleteMapping(path = "tenants/{tenantName}/organizations/{orgExternalKey}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteOrganization(@PathVariable String tenantName, @PathVariable String orgExternalKey) {
		Organization org = validateOrganization(tenantName, orgExternalKey);
		org.getTenant().getOrganizations().remove(org);
		orgRepo.delete(org);
	}

	@GetMapping(path = "tenants/{tenantName}/organizations/{orgExternalKey}")
	public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable String tenantName, @PathVariable String orgExternalKey) {
		return new ResponseEntity<>(new OrganizationResponse(validateOrganization(tenantName, orgExternalKey)), HttpStatus.OK);
	}
	
	
	@PostMapping(path = "tenants/{tenantName}/applications")
	public ResponseEntity<CreateApplicationResponse> createApplication(@PathVariable String tenantName,	@RequestBody CreateApplicationRequest createApplicationReq) {
		Organization org = validateOrganization(tenantName, createApplicationReq.getOrganization());
		User owner = validateUser(tenantName, createApplicationReq.getOwner());
		Application app = createApplicationReq.getApplication();
		app.setOrganization(org);
		org.getApplications().add(app);
		app.setOwner(owner);
		app.setTenant(org.getTenant());
		return new ResponseEntity<>(new CreateApplicationResponse(appRepo.save(app)), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path = "tenants/{tenantName}/applications/{appExternalKey}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteApplication(@PathVariable String tenantName, @PathVariable String appExternalKey) {
		appRepo.delete(validateApplication(tenantName, appExternalKey));
	}
	
	
	@GetMapping(path = "tenants/{tenantName}/applications/{appExternalId}")
	public ResponseEntity<ApplicationResponse> getApplication(@PathVariable String tenantName, @PathVariable String appExternalId) {
		return new ResponseEntity<>(new ApplicationResponse(validateApplication(tenantName, appExternalId)), HttpStatus.OK);
	}
	
	
	private Tenant validateTenant(String tenantName) {
		Optional<Tenant> tenant = tenantRepo.findByName(tenantName);
		if (tenant.isPresent()) {
			return tenant.get(); 
		} else {
			throw new InvalidTenantException("Tenant does not exist: " + tenantName);
		}
	}

	private Organization validateOrganization(String tenantName, String orgExternalKey) {
		Optional<Organization> org = orgRepo.findByTenantNameAndOrgExternalId(tenantName, orgExternalKey);
		
		if (org.isPresent()) {
			return org.get(); 
		} else {
			throw new InvalidOrganizationException(	"Organization , " + orgExternalKey + ", does not belong to the tenant " + tenantName);
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
	
	private Application validateApplication(String tenantName, String appExternalKey) {
		Optional<Application> app = appRepo.findByTenantNameAndAppExternalId(tenantName, appExternalKey);		
		if (app.isPresent()) {
			return app.get(); 
		} else {
			throw new InvalidApplicationException("Invalid Application(" + tenantName + ", " + appExternalKey + ")");
		}
	}
}
