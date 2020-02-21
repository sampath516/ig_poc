package com.poc.ig.certification.events.consumer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.certification.client.RepositoryRestClient;
import com.poc.ig.certification.dto.ApplicationResponse;
import com.poc.ig.certification.dto.UserResourceEntitlement;
import com.poc.ig.certification.dto.UserResourceEntitlement.ResourceDto;
import com.poc.ig.certification.dto.UserResourceEntitlement.UserDto;
import com.poc.ig.certification.dto.UserResponse;
import com.poc.ig.certification.entity.Application;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Entitlement.EntitlementState;
import com.poc.ig.certification.entity.Entitlement.EntitlementType;
import com.poc.ig.certification.entity.Resource;
import com.poc.ig.certification.entity.Review;
import com.poc.ig.certification.entity.Review.ReviewState;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;
import com.poc.ig.certification.events.Events;
import com.poc.ig.certification.events.KafkaTopics;
import com.poc.ig.certification.events.dto.Event;
import com.poc.ig.certification.events.dto.UserResourceEntitlements;
import com.poc.ig.certification.repository.ApplicationRepository;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.repository.EntitlementRepository;
import com.poc.ig.certification.repository.OrganizationRepository;
import com.poc.ig.certification.repository.ResourceRepository;
import com.poc.ig.certification.repository.ReviewRepository;
import com.poc.ig.certification.repository.TenantRepository;
import com.poc.ig.certification.repository.UserRepository;
import com.poc.ig.certification.service.ServicesUtil;

@Component
public class UserResourceEntitlementsConsumer {

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private EntitlementRepository entitlementRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CertificationRepository certRepo;

	@Autowired
	RepositoryRestClient repositoryClient;

	@Autowired
	private ApplicationRepository appRepo;

	@Autowired
	private ResourceRepository resRepo;

	@Autowired
	ReviewRepository reviewRepo;
	
	@Autowired
	ObjectMapper jsonObjectMapper;

	@KafkaListener(containerFactory = "kafkaEventsListenerContainerFactory", topics = KafkaTopics.ENTITLEMENTS_EVENT_TOPIC)
	public void listen(@SuppressWarnings("rawtypes") Event event) {
		if (event.getName().equals(Events.USER_RESOURCE_ENTITLEMENTS_EVENT)) {
			UserResourceEntitlements entitlementsObj = (UserResourceEntitlements) event.getEventData(jsonObjectMapper);
			String certificationName = entitlementsObj.getCertificationName();
			String tenantName = entitlementsObj.getTenantName();
			//String orgName = entitlementsObj.getOrganizationName();
			List<UserResourceEntitlement> entitlements = entitlementsObj.getEntitlements();

			Certification cert = ServicesUtil.validateCertification(certRepo, tenantName, certificationName);

			for (UserResourceEntitlement entmt : entitlements) {

				// Primary Entity - User
				User primaryEntity = mapToUserEntity(entmt.getPrimaryEntity(), cert);
				setManager(primaryEntity, entmt.getPrimaryEntity().getManager(), cert);
				primaryEntity = userRepo.save(primaryEntity);

				// Secondary Entity - Resource
				Resource secondaryEntity = mapToResourceEntity(entmt.getSecondaryEntity(), cert);
				setResourceOwner(secondaryEntity, entmt.getSecondaryEntity().getOwner(), cert);
				setApplication(secondaryEntity, entmt.getSecondaryEntity().getApplication(), cert);
				secondaryEntity = resRepo.save(secondaryEntity);

				// Creating Entitlement
				UserPrivilegesResourceEntitlement entitlement = createUserResEntitlement(primaryEntity, secondaryEntity, cert);
				entitlementRepo.save(entitlement);
				Set<User> reviewers = ServicesUtil.getReviewersForUserPrivResEntitlement(primaryEntity, secondaryEntity);
				for (User reviewer : reviewers) {
					createReview(entitlement, cert, reviewer);
				}
				entitlementRepo.save(entitlement);
			}

		}
	}

	private User mapToUserEntity(UserDto userDto, Certification cert) {
		User user = new User();
		user.setExternalId(userDto.getExternalId());
		user.setName(userDto.getName());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());

		user.setTenant(cert.getTenant());
		user.setTenantName(cert.getTenantName());
		user.setOrganization(cert.getOrganization());
		cert.getOrganization().getUsers().add(user);

		user.setCertification(cert);
		user.setCertificationName(cert.getName());

		return user;
	}

	private User setManager(User user, String managerIn, Certification cert) {
		if (managerIn != null && !managerIn.trim().isEmpty()) {
			String userId = user.getTenantName() + user.getCertificationName() + managerIn;
			Optional<User> userContainer = userRepo.findById(userId.toLowerCase());
			if (userContainer.isPresent()) {
				user.setManager(userContainer.get());
			} else {
				User managerEntity = null;
				User userEntity = user;
				String manager = managerIn;
				while (manager != null) {
					UserResponse managerDto = repositoryClient.getUser(user.getTenantName(), manager);
					managerEntity = mapToUserEntity(managerDto);
					managerEntity.setCertification(cert);
					managerEntity.setCertificationName(cert.getName());
					managerEntity = userRepo.save(managerEntity);
					userEntity.setManager(managerEntity);
					userRepo.save(userEntity);
					userEntity = managerEntity;
					manager = managerDto.getManager();
				}
			}

		}
		return user;
	}

	private Resource mapToResourceEntity(ResourceDto resDto, Certification cert) {

		Resource res = new Resource();

		res.setExternalId(resDto.getExternalId());
		res.setName(resDto.getName());
		res.setDescription(resDto.getDescription());

		res.setCertification(cert);
		res.setCertificationName(cert.getName());

		res.setTenant(cert.getTenant());
		res.setTenantName(cert.getTenantName());

		return res;

	}

	private void setApplication(Resource res, String appExternalId, Certification cert) {
		String appId = cert.getTenantName() + cert.getName() + appExternalId;
		Optional<Application> appContainer = appRepo.findById(appId.toLowerCase());
		Application appEntity = null;
		if (appContainer.isPresent()) {
			appEntity = appContainer.get();
			res.setApplication(appEntity);
			appEntity.getResources().add(res);
		} else {
			ApplicationResponse appRes = repositoryClient.getApplication(cert.getTenantName(), appExternalId);
			appEntity = mapToApplicationEntity(appRes, res, cert);
			setOwner(appEntity, appRes.getOwner(), cert);
		}
		appRepo.save(appEntity);
	}

	private void setResourceOwner(Resource res, String owner, Certification cert) {
		String ownerId = cert.getTenantName() + cert.getName() + owner;
		Optional<User> ownerrContainer = userRepo.findById(ownerId.toLowerCase());
		User ownerEntity = null;
		if (ownerrContainer.isPresent()) {
			ownerEntity = ownerrContainer.get();
			res.setOwner(ownerEntity);
			ownerEntity.getResources().add(res);
		} else {

			UserResponse ownerDto = repositoryClient.getUser(cert.getTenantName(), owner);
			ownerEntity = mapToUserEntity(ownerDto);
			ownerEntity.setCertification(cert);
			ownerEntity.setCertificationName(cert.getName());
			res.setOwner(ownerEntity);
			ownerEntity.getResources().add(res);
			setManager(ownerEntity, ownerDto.getManager(), cert);
		}
	}

	private Application mapToApplicationEntity(ApplicationResponse appRes, Resource res, Certification cert) {
		Application appEntity = new Application();
		appEntity.setExternalId(appRes.getExternalId());
		appEntity.setName(appRes.getName());
		appEntity.setDescription(appRes.getDescription());

		appEntity.setTenant(cert.getTenant());
		appEntity.setTenantName(cert.getTenantName());
		appEntity.setOrganization(cert.getOrganization());

		appEntity.setCertificationName(cert.getName());
		res.setApplication(appEntity);
		appEntity.getResources().add(res);
		return appEntity;
	}

	private void setOwner(Application appEntity, String owner, Certification cert) {
		if (owner != null && !owner.trim().isEmpty()) {
			String ownerId = cert.getTenantName() + cert.getName() + owner;
			Optional<User> ownerContainer = userRepo.findById(ownerId.toLowerCase());
			if (ownerContainer.isPresent()) {
				appEntity.setOwner(ownerContainer.get());
			} else {
				UserResponse ownerDto = repositoryClient.getUser(cert.getTenantName(), owner);
				User ownerEntity = mapToUserEntity(ownerDto);
				ownerEntity.setCertification(cert);
				ownerEntity.setCertificationName(cert.getName());
				appEntity.setOwner(ownerEntity);
				setManager(ownerEntity, ownerDto.getManager(), cert);
			}
		}
	}

	private User mapToUserEntity(UserResponse userDto) {
		User user = new User();
		user.setExternalId(userDto.getExternalId());
		user.setName(userDto.getName());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setTenant(ServicesUtil.validateTenant(tenantRepo, userDto.getTenantName()));
		user.setOrganization(
				ServicesUtil.validateOrganization(orgRepo, userDto.getTenantName(), userDto.getOrganization()));
		user.setTenantName(userDto.getTenantName());
		return user;
	}

	private UserPrivilegesResourceEntitlement createUserResEntitlement(User primaryEntity, Resource secondaryEntity,
			Certification cert) {
		UserPrivilegesResourceEntitlement entitlement = new UserPrivilegesResourceEntitlement();

		entitlement.setTenantName(cert.getTenantName());
		entitlement.setCertificationName(cert.getName());

		entitlement.setCertification(cert);
		entitlement.setEntitlementType(EntitlementType.USER_RESOURCE);

		entitlement.setPrimaryEntity(primaryEntity);
		entitlement.setSecondaryEntity(secondaryEntity);

		entitlement.setState(EntitlementState.OPEN);
		return entitlement;
	}

	private Review createReview(UserPrivilegesResourceEntitlement entitlement, Certification cert, User reviewer) {
		Review review = new Review();

		review.setState(ReviewState.OPEN);

		review.setTenantName(cert.getTenantName());
		review.setCertificationName(cert.getName());

		review.setEntitlement(entitlement);
		entitlement.getReviews().add(review);
		review.setReviewer(reviewer);

		reviewer.setReviewCertification(cert);
		reviewRepo.save(review);
		return review;
	}
}
