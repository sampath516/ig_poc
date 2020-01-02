package com.poc.ig.certification.service;

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

import com.poc.ig.certification.dto.CreateApplicationRequest;
import com.poc.ig.certification.dto.CreateApplicationResponse;
import com.poc.ig.certification.dto.CreateOrganizationRequest;
import com.poc.ig.certification.dto.CreateOrganizationResponse;
import com.poc.ig.certification.dto.CreateTenantRequest;
import com.poc.ig.certification.dto.CreateTenantResponse;
import com.poc.ig.certification.dto.GetTenantResponse;
import com.poc.ig.certification.dto.OrganizationResponse;
import com.poc.ig.certification.entity.Application;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Organization;
import com.poc.ig.certification.entity.Tenant;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.exception.InvalidOrganizationException;
import com.poc.ig.certification.exception.InvalidTenantException;
import com.poc.ig.certification.exception.InvalidUserException;
import com.poc.ig.certification.repository.ApplicationRepository;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.repository.OrganizationRepository;
import com.poc.ig.certification.repository.TenantRepository;
import com.poc.ig.certification.repository.UserRepository;



@RestController
@RequestMapping(value = "ig/certification/v1/", produces = "application/json", consumes = "application/json")
public class TenantService {

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private OrganizationRepository orgRepo;
	
	@Autowired
	private ApplicationRepository appRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CertificationRepository certRepo;

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

	@PostMapping(path = "tenants/{tenantName}/organizations")
	public ResponseEntity<CreateOrganizationResponse> createOrganization(@PathVariable String tenantName,	@RequestBody CreateOrganizationRequest createOrganizationReq) {
		Tenant tenant = validateTenant(tenantName);
		Organization org = createOrganizationReq.getOrganization();
		org.setTenant(tenant);
		org.setTenantName(tenantName);
		return new ResponseEntity<>(new CreateOrganizationResponse(orgRepo.save(org)), HttpStatus.CREATED);
	}

	@GetMapping(path = "tenants/{tenantName}/organizations/{orgExternalKey}")
	public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable String tenantName, @PathVariable String orgExternalKey) {
		return new ResponseEntity<OrganizationResponse>(new OrganizationResponse(validateOrganization(tenantName, orgExternalKey)), HttpStatus.OK);
	}
	
	@PostMapping(path = "tenants/{tenantName}/applications")
	public ResponseEntity<CreateApplicationResponse> createApplication(@PathVariable String tenantName,	@RequestBody CreateApplicationRequest createApplicationReq) {
		Organization org = validateOrganization(tenantName, createApplicationReq.getOrganization());
		Certification cert = ServicesUtil.validateCertification(certRepo, tenantName, createApplicationReq.getCertificationName());
		User owner = validateUser(tenantName, createApplicationReq.getOwner());
		Application app = createApplicationReq.getApplication();
		app.setOrganization(org);
		org.getApplications().add(app);
		app.setOwner(owner);
		app.setTenant(org.getTenant());
		app.setTenantName(tenantName);
		app.setCertificationName(cert.getName());
		return new ResponseEntity<>(new CreateApplicationResponse(appRepo.save(app)), HttpStatus.CREATED);
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
	
//	private Application validateApplication(String tenantName, String appExternalKey) {
//		Optional<Application> app = appRepo.findByTenantNameAndAppExternalId(tenantName, appExternalKey);		
//		if (app.isPresent()) {
//			return app.get(); 
//		} else {
//			throw new InvalidApplicationException("Invalid Application(" + tenantName + ", " + appExternalKey + ")");
//		}
//	}
}
