package com.poc.ig.certification.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.poc.ig.certification.dto.EntitlementRequestDto.Entity;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Entitlement;
import com.poc.ig.certification.entity.Organization;
import com.poc.ig.certification.entity.Resource;
import com.poc.ig.certification.entity.Tenant;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;
import com.poc.ig.certification.exception.InvalidCertificationException;
import com.poc.ig.certification.exception.InvalidOrganizationException;
import com.poc.ig.certification.exception.InvalidResourceException;
import com.poc.ig.certification.exception.InvalidTenantException;
import com.poc.ig.certification.exception.InvalidUserException;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.repository.EntitlementRepository;
import com.poc.ig.certification.repository.OrganizationRepository;
import com.poc.ig.certification.repository.ResourceRepository;
import com.poc.ig.certification.repository.TenantRepository;
import com.poc.ig.certification.repository.UserPrivilegesResourceEntitlementRepository;
import com.poc.ig.certification.repository.UserRepository;

public class ServicesUtil {

	public static Organization validateOrganization(OrganizationRepository orgRepo, String tenantName,
			String orgExternalId) {
		Optional<Organization> org = orgRepo.findByTenantNameAndOrgExternalId(tenantName, orgExternalId);

		if (org.isPresent()) {
			return org.get();
		} else {
			throw new InvalidOrganizationException("Invalid Organizatio(" + tenantName + ", " + orgExternalId + ")");
		}
	}

	public static Tenant validateTenant(TenantRepository tenantRepo, String tenantName) {
		Optional<Tenant> tenant = tenantRepo.findByName(tenantName);
		if (tenant.isPresent()) {
			return tenant.get();
		} else {
			throw new InvalidTenantException("Invalid Tenant: " + tenantName);
		}
	}

	public static User validateUser(UserRepository userRepo, String tenantName, String userExternalId) {
		Optional<User> user = userRepo.findByTenantNameAndUserExternalId(tenantName, userExternalId);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
	}
	
	public static Certification validateCertification_v1(CertificationRepository certRepo, String tenantName, String certificationName) {
		Optional<Certification> cert = certRepo.findByTenantNameAndName(tenantName, certificationName);
		if (cert.isPresent()) {
			return cert.get();
		} else {
			throw new InvalidCertificationException("Invalid Certification(" + tenantName + ", " + certificationName + ")");
		}
	}
	
	public static Certification validateCertification(CertificationRepository certRepo, String tenantName, String certificationName) {
		String certificationId = tenantName+certificationName;
		Optional<Certification> cert = certRepo.findById(certificationId.toLowerCase());
		if (cert.isPresent()) {
			return cert.get();
		} else {
			throw new InvalidCertificationException("Invalid Certification(" + tenantName + ", " + certificationName + ")");
		}
	}
	
	public static Certification findCertificationById(CertificationRepository certRepo, String tenantName, String certificationName, int depth) {
		String certificationId = tenantName+certificationName;
		Optional<Certification> cert = certRepo.findById(certificationId.toLowerCase(), depth);
		if (cert.isPresent()) {
			return cert.get();
		} else {
			throw new InvalidCertificationException("Invalid Certification(" + tenantName + ", " + certificationName + ")");
		}
	}
	
	public static User getUserByTenantNameAndExternalId(UserRepository userRepo, String tenantName, String userExternalId) {
		Optional<User> userTemp = userRepo.findByExternalId(userExternalId);
		User user = null;
		if (userTemp.isPresent()) {
			user = userTemp.get();
			if (!user.getTenant().getName().equals(tenantName)) {
				throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
			}
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
		return user;
	}
	
	public static User findUserByTenantNameAndCertNameAndExternalId(UserRepository userRepo, String tenantName, String userExternalId, String certName) {
		Optional<User> userTemp = userRepo.findByTenantNameAndCertNameAndUserExternalId(tenantName, userExternalId, certName);
		User user = null;
		if (userTemp.isPresent()) {
			user = userTemp.get();
			if (!user.getTenant().getName().equals(tenantName)) {
				throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
			}
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
		return user;
	}
	
	
	public static Resource findResourceByTenantNameAndCertNameAndExternalId(ResourceRepository resRepo, String tenantName, String resExternalId, String certName) {
		Optional<Resource> resContainer = resRepo.findByTenantNameAndCertNameAndUserExternalId(tenantName, resExternalId, certName);
		Resource resource = null;
		if (resContainer.isPresent()) {
			resource = resContainer.get();
			if (!resource.getTenant().getName().equals(tenantName)) {
				throw new InvalidResourceException("Invalid Resource(" + tenantName + ", " + resExternalId + ")");
			}
		} else {
			throw new InvalidResourceException("Invalid Resource(" + tenantName + ", " + resExternalId + ")");
		}
		return resource;
	}
	
	public static Set<User> getReviewersForUserPrivResEntitlement(User primaryEntity, Resource secondaryEntity){
		Set<User> reviewers = new HashSet<User>();
		if(primaryEntity.getManager() != null) {
			reviewers.add(primaryEntity.getManager());
		}
		if(secondaryEntity.getOwner() != null){
			reviewers.add(secondaryEntity.getOwner());
		}		
		return reviewers;
	}
	
	public static Entitlement findEntitlement(EntitlementRepository entmtRepo, String entitlementkey) {
		Optional<Entitlement> entmtContainer = entmtRepo.findById(entitlementkey, 1);
		Entitlement entmt = null;
		if (entmtContainer.isPresent()) {
			entmt = entmtContainer.get();
		}
		return entmt;
	}
	
	public static Entitlement findUserPrevResEntitlement(UserPrivilegesResourceEntitlementRepository entmtRepo, String entitlementkey) {
		Optional<UserPrivilegesResourceEntitlement> entmtContainer = entmtRepo.findById(entitlementkey, 1);
		Entitlement entmt = null;
		if (entmtContainer.isPresent()) {
			entmt = entmtContainer.get();
		}
		return entmt;
	}
	
	
	public static User getUserEntity(Entity entity, String tenantName, String certName, TenantRepository tenantRepo, OrganizationRepository orgRepo, UserRepository userRepo) {
		return findUserByTenantNameAndCertNameAndExternalId(userRepo,tenantName, entity.getExternalId(), certName); 
	}

}
