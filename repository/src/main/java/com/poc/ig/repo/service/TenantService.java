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

import com.poc.ig.repo.dto.CreateOrganizationRequest;
import com.poc.ig.repo.dto.CreateOrganizationResponse;
import com.poc.ig.repo.dto.CreateTenantRequest;
import com.poc.ig.repo.dto.CreateTenantResponse;
import com.poc.ig.repo.dto.GetTenantResponse;
import com.poc.ig.repo.dto.ListOrganizationsResponse;
import com.poc.ig.repo.dto.ListTenantsResponse;
import com.poc.ig.repo.dto.OrganizationResponse;
import com.poc.ig.repo.dto.UpdateTenantRequest;
import com.poc.ig.repo.dto.UpdateTenantResponse;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Tenant;
import com.poc.ig.repo.exception.InvalidOrganizationIdException;
import com.poc.ig.repo.exception.InvalidTenantIdException;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.TenantRepository;


@RestController
@RequestMapping(value = "ig/repo/v1/", produces = "application/json", consumes = "application/json")
public class TenantService {

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private OrganizationRepository orgRepo;

	@PostMapping(path = "tenants")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateTenantResponse createTenant(@RequestBody CreateTenantRequest createTenantReq) {
		return new CreateTenantResponse(tenantRepo.save(createTenantReq.tenant()));
	}

	@GetMapping(path = "tenants/{tenantId}")
	public ResponseEntity<GetTenantResponse> getTenant(@PathVariable long tenantId) {
		Tenant tenant = validateTenant(tenantId);
		return new ResponseEntity<>(new GetTenantResponse(tenant), HttpStatus.OK);

	}
	
	@PutMapping(path = "tenants")
	@ResponseStatus(HttpStatus.OK)
	public UpdateTenantResponse updateTenant(@RequestBody UpdateTenantRequest updateTenantReq) {
		return new UpdateTenantResponse(tenantRepo.save(updateTenantReq.getTenant()));
	}
	
	@GetMapping(path = "tenants")
	@ResponseStatus(HttpStatus.OK)
	public ListTenantsResponse listTenants() {
		return new ListTenantsResponse(tenantRepo.findAll());
	}

	@DeleteMapping(path = "tenants/{tenantId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTenant(@PathVariable long tenantId) {
		tenantRepo.delete(validateTenant(tenantId));
	}

	@GetMapping(path = "tenants/{tenantId}/organizations")
	@ResponseStatus(HttpStatus.OK)
	public ListOrganizationsResponse listOrganizations(@PathVariable long tenantId) {
		validateTenant(tenantId);
		return new ListOrganizationsResponse(orgRepo.findByTenantId(tenantId));
	}

	@PostMapping(path = "tenants/{tenantId}/organizations")
	public ResponseEntity<CreateOrganizationResponse> addOrganization(@PathVariable long tenantId,
			@RequestBody CreateOrganizationRequest createOrganizationReq) {
		Tenant tenant = validateTenant(tenantId);
		Organization org = createOrganizationReq.getOrganization();
		org.setTenant(tenant);
		return new ResponseEntity<>(new CreateOrganizationResponse(orgRepo.save(org)), HttpStatus.CREATED);
	}

	@DeleteMapping(path = "tenants/{tenantId}/organizations/{orgId}")
	@ResponseStatus(HttpStatus.OK)
	public void removeOrganization(@PathVariable long tenantId, @PathVariable long orgId) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		org.getTenant().getOrganizations().remove(org);
		orgRepo.delete(org);
	}

	@GetMapping(path = "tenants/{tenantId}/organizations/{orgId}")
	public ResponseEntity<OrganizationResponse> getOrganization(@PathVariable long tenantId,
			@PathVariable long orgId) {
		return new ResponseEntity<>(new OrganizationResponse(validateTenantOrganization(tenantId, orgId)),
				HttpStatus.OK);
	}

	private Tenant validateTenant(long tenantId) {
		Optional<Tenant> tenant = tenantRepo.findById(tenantId);
		if (tenant.isPresent()) {
			return tenant.get(); 
		} else {
			throw new InvalidTenantIdException("Tenant does not exist: " + tenantId);
		}
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
