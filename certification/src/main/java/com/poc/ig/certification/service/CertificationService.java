package com.poc.ig.certification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.ig.certification.client.RepositoryRestClient;
import com.poc.ig.certification.dto.ApplicationResponse;
import com.poc.ig.certification.dto.CertificationResponse;
import com.poc.ig.certification.dto.CreateCertificationRequest;
import com.poc.ig.certification.dto.GetReviewsByCertificationAndReviewerResponse;
import com.poc.ig.certification.dto.ReviewRequest;
import com.poc.ig.certification.dto.ReviewRequest.Action;
import com.poc.ig.certification.dto.ReviewResponse;
import com.poc.ig.certification.dto.ReviewResponse.Entity;
import com.poc.ig.certification.dto.ReviewResponse.EntityType;
import com.poc.ig.certification.dto.UserResourceEntitlement;
import com.poc.ig.certification.dto.UserResourceEntitlement.ResourceDto;
import com.poc.ig.certification.dto.UserResourceEntitlement.UserDto;
import com.poc.ig.certification.dto.UserResourceEntitlementRequest;
import com.poc.ig.certification.dto.UserResponse;
import com.poc.ig.certification.entity.Application;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Entitlement;
import com.poc.ig.certification.entity.Entitlement.EntitlementState;
import com.poc.ig.certification.entity.Entitlement.EntitlementType;
import com.poc.ig.certification.entity.Organization;
import com.poc.ig.certification.entity.Resource;
import com.poc.ig.certification.entity.Review;
import com.poc.ig.certification.entity.Review.ActionType;
import com.poc.ig.certification.entity.Review.ReviewLogic;
import com.poc.ig.certification.entity.Review.ReviewState;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;
import com.poc.ig.certification.events.Events;
import com.poc.ig.certification.events.dto.CertificationDto;
import com.poc.ig.certification.events.dto.Event;
import com.poc.ig.certification.events.producer.CertificationEventsProducer;
import com.poc.ig.certification.events.producer.EntitlementsRejectionProducer;
import com.poc.ig.certification.repository.ApplicationRepository;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.repository.EntitlementRepository;
import com.poc.ig.certification.repository.OrganizationRepository;
import com.poc.ig.certification.repository.ResourceRepository;
import com.poc.ig.certification.repository.ReviewRepository;
import com.poc.ig.certification.repository.TenantRepository;
import com.poc.ig.certification.repository.UserPrivilegesResourceEntitlementRepository;
import com.poc.ig.certification.repository.UserRepository;


@RestController
@RequestMapping(value = "ig/certification/v1/tenants/{tenantName}/", produces = "application/json", consumes = "application/json")
public class CertificationService {

	@Autowired
	private CertificationRepository certRepo;

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResourceRepository resRepo;
	
	@Autowired
	private ApplicationRepository appRepo;
	
	@Autowired
	private EntitlementRepository entmtRepo;
	
	@Autowired
	private RepositoryRestClient repositoryClient;
	
	@Autowired
	private ReviewRepository reviewRepo;
	

	@Autowired
	private CertificationEventsProducer certEventsProducer;
	
	@Autowired
	private ObjectMapper jsonObjectMapper;
	
	@Autowired
	private EntitlementsRejectionProducer entitlementsRejProducer; 
	
	@Autowired
	private UserPrivilegesResourceEntitlementRepository userPrevResEntitlementRepo;


	@PostMapping(path = "certifications")
	@ResponseStatus(HttpStatus.CREATED)
	public CertificationResponse createCertification(@PathVariable String tenantName, @RequestBody CreateCertificationRequest certificationRequest) {

		Certification cert = new Certification();
		cert.setName(certificationRequest.getName());
		cert.setDescription(certificationRequest.getDescription());
		cert.setType(certificationRequest.getCertificationType());
		
		cert.setTenant(ServicesUtil.validateTenant(tenantRepo, tenantName));
		cert.setTenantName(tenantName);

		Organization org = ServicesUtil.validateOrganization(orgRepo, tenantName, certificationRequest.getOrganization());
		cert.setOrganization(org);
		User admin = createAdminForCertification(cert);
		createApplicationForCertification(cert, admin);
		cert.setOwner(admin);
		cert = certRepo.save(cert);
		//Temporarily commenting it.
//		UserResponse ownerDto = repositoryClient.getUser(tenantName, certificationRequest.getOwner());
//		User ownerEntity = mapToUserEntity(ownerDto);
//		ownerEntity.setCertification(cert);
//		ownerEntity.setCertificationName(certificationRequest.getName());
//		cert.setOwner(ownerEntity);
//		cert.setState(CertificationState.CREATE);
//		userRepo.save(ownerEntity);
//		cert = certRepo.save(cert);
//		
//		//Save the Manager hierarchy
//		String manager = ownerDto.getManager();		
//		User managerEntity = null;
//		while(manager != null && !manager.isEmpty()) {
//			UserResponse managerDto = repositoryClient.getUser(tenantName, manager);
//			managerEntity = mapToUserEntity(managerDto);
//			managerEntity.setCertification(cert);
//			managerEntity.setCertificationName(cert.getName());
//			managerEntity = userRepo.save(managerEntity);
//			ownerEntity.setManager(managerEntity);
//			userRepo.save(ownerEntity);
//			ownerEntity = managerEntity;
//			manager = managerDto.getManager();			
//		}	
		certEventsProducer.send(new Event<CertificationDto>(Events.NEW_CERTITICATION_EVENT, new CertificationDto(cert), jsonObjectMapper));
		return new CertificationResponse(cert);
	}

	@PostMapping(path = "certifications/{certification}/user-res-entitlements")
	@ResponseStatus(HttpStatus.CREATED)
	public void createUserResourceEntitlements(@PathVariable String tenantName, @PathVariable String certification, @RequestBody UserResourceEntitlementRequest userResEntitlementRequest) {
		
		Certification cert = ServicesUtil.validateCertification(certRepo, userResEntitlementRequest.getTenantName(), userResEntitlementRequest.getCertification());
		List<UserResourceEntitlement> entitlements = userResEntitlementRequest.getEntitlements();
		
		for(UserResourceEntitlement entmt : entitlements) {
			
			//Primary Entity - User
			User primaryEntity = mapToUserEntity(entmt.getPrimaryEntity(), cert);
			setManager(primaryEntity, entmt.getPrimaryEntity().getManager(), cert);
			primaryEntity = userRepo.save(primaryEntity);
			
			//Secondary Entity - Resource			
			Resource secondaryEntity = mapToResourceEntity(entmt.getSecondaryEntity(), cert);
			setResourceOwner(secondaryEntity, entmt.getSecondaryEntity().getOwner(), cert);
			setApplication(secondaryEntity, entmt.getSecondaryEntity().getApplication(), cert);
			secondaryEntity = resRepo.save(secondaryEntity);
			
			//Creating Entitlement
			UserPrivilegesResourceEntitlement entitlement = createUserResEntitlement(primaryEntity, secondaryEntity, cert);
			entmtRepo.save(entitlement);
			Set<User> reviewers =ServicesUtil.getReviewersForUserPrivResEntitlement(primaryEntity, secondaryEntity);			
			for(User reviewer: reviewers) {
				createReview(entitlement, cert, reviewer);
			}
			entmtRepo.save(entitlement);			
		}
	}
	
	@GetMapping(path = "certifications/{certification}/reviewers/{reviewer}/reviews")
	public ResponseEntity<GetReviewsByCertificationAndReviewerResponse>  getReviewsByCertificationAndReviewer(@PathVariable String tenantName, @PathVariable String certification, @PathVariable String reviewer, @RequestBody  @RequestParam(defaultValue = "OPEN") ReviewState reviewState) {
		
		//Total review count by Reviewer
		//All reviews by Reviewer
		GetReviewsByCertificationAndReviewerResponse response = new GetReviewsByCertificationAndReviewerResponse();
		response.setCertification(certification);
		response.setTenant(tenantName);
		response.setReviewer(reviewer);
	    Set<Review> reviews =	reviewRepo.findAllUserResourceReviews(tenantName, certification, reviewer, reviewState);
	    
	    for( Review rev: reviews) {
	    	ReviewResponse reviewRes = new ReviewResponse();
	    	reviewRes.setReviewId(rev.getKey());
	    	reviewRes.setReviewState(rev.getState());
	    	User user = ((UserPrivilegesResourceEntitlement)rev.getEntitlement()).getPrimaryEntity();
	    	Resource res = ((UserPrivilegesResourceEntitlement)rev.getEntitlement()).getSecondaryEntity();
	    	
	    	Entity primaryEntity = new Entity();
	    	primaryEntity.setEntityType(EntityType.USER);
	    	primaryEntity.setExternalId(user.getExternalId());
	    	primaryEntity.setName(user.getName());
	    	primaryEntity.setFirstName(user.getFirstName());	    	
	    	primaryEntity.setLastName(user.getLastName());
	    	if(user.getOrganization() != null) {
	    		primaryEntity.setOrganization(user.getOrganization().getName());
	    	}
	    	
	    	Entity secondaryEntity = new Entity();
	    	secondaryEntity.setEntityType(EntityType.RESOURCE);
	    	secondaryEntity.setExternalId(res.getExternalId());
	    	secondaryEntity.setName(res.getName());
	    	secondaryEntity.setDescription(res.getDescription());
	    	
	    	reviewRes.setPrimaryEntity(primaryEntity);
	    	reviewRes.setSecondaryEntity(secondaryEntity);
	    	response.getReviews().add(reviewRes);
	    	
	    }
	    response.setTotal(response.getReviews().size());
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping(path = "certifications/{certification}/reviewers/{reviewer}/reviews")
	@ResponseStatus(HttpStatus.OK)
	public void updateReview(@PathVariable String tenantName, @PathVariable String certification, @PathVariable String reviewer, @RequestBody  ReviewRequest reviewRequest) {
		Action action =   reviewRequest.getAction();
		List<String> reviewIds = reviewRequest.getReviewIds();
		Iterable<Review> reviews = reviewRepo.findAllById(reviewIds, 1);
		if(action.getType().equals(ActionType.APPROVE) || action.getType().equals(ActionType.REJECT)) {
			for(Review  r: reviews) {
				r.setActionType(action.getType());
				r.setState(ReviewState.CLOSED);
			}
			reviews = reviewRepo.saveAll(reviews);
		}
		updateEntitlements(tenantName, reviews, certification);
	}
	
	private void updateEntitlements(String tenantName, Iterable<Review> reviews, String certificationName) {
		List<Entitlement> entmts = new ArrayList<Entitlement>();
		List<Entitlement> rejectedEntitlements = new ArrayList<Entitlement>();
		for (Review r : reviews) {
			Entitlement entmt = null;
			if(r.getEntitlement().getEntitlementType().equals(EntitlementType.USER_RESOURCE)) {
				entmt = ServicesUtil.findUserPrevResEntitlement(userPrevResEntitlementRepo, r.getEntitlement().getKey());
			}
			
			Set<Review> allEntitlementReviews = entmt.getReviews();
			if (r.getLogic().equals(ReviewLogic.AND)) {
				if (r.getActionType().equals(ActionType.REJECT)) {
					entmt.setActionType(com.poc.ig.certification.entity.Entitlement.ActionType.REJECT);
					entmt.setState(EntitlementState.CLOSED);
					entmts.add(entmt);
					rejectedEntitlements.add(entmt);
				} else if (r.getActionType().equals(ActionType.APPROVE) && allApproved(allEntitlementReviews)) {
					entmt.setActionType(com.poc.ig.certification.entity.Entitlement.ActionType.APPROVE);
					entmt.setState(EntitlementState.CLOSED);
					entmts.add(entmt);
				}

			}
			if (r.getLogic().equals(ReviewLogic.OR)) {
				if (r.getActionType().equals(ActionType.APPROVE)) {
					entmt.setActionType(com.poc.ig.certification.entity.Entitlement.ActionType.APPROVE);
					entmt.setState(EntitlementState.CLOSED);
					entmts.add(entmt);
				} else if (r.getActionType().equals(ActionType.REJECT) && allRejected(allEntitlementReviews)) {
					entmt.setActionType(com.poc.ig.certification.entity.Entitlement.ActionType.REJECT);
					entmt.setState(EntitlementState.CLOSED);
					entmts.add(entmt);
					rejectedEntitlements.add(entmt);
				}
				

			}
		}
		entmtRepo.saveAll(entmts);
		if(!rejectedEntitlements.isEmpty()) {
			entitlementsRejProducer.sendEvents(tenantName, rejectedEntitlements, jsonObjectMapper, certificationName);
		}		
	}
	
	private boolean allApproved(Set<Review> reviews) {
		boolean result = true;
		for(Review r: reviews) {
			if(r.getState().equals(ReviewState.OPEN) || r.getActionType().equals(ActionType.REJECT)) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	private boolean allRejected(Set<Review> reviews) {
		boolean result = true;
		for(Review r: reviews) {
			if(r.getState().equals(ReviewState.OPEN) || r.getActionType().equals(ActionType.APPROVE)) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	private UserPrivilegesResourceEntitlement createUserResEntitlement(User primaryEntity, Resource secondaryEntity, Certification cert) {
		UserPrivilegesResourceEntitlement entitlement = new UserPrivilegesResourceEntitlement();
		
		
		entitlement.setTenantName(cert.getTenantName());
		entitlement.setCertificationName(cert.getName());
		
		entitlement.setCertification(cert);
		
		entitlement.setPrimaryEntity(primaryEntity);
		entitlement.setSecondaryEntity(secondaryEntity);
		
		entitlement.setState(EntitlementState.OPEN);
		return entitlement;
	}
	
	private Review createReview(UserPrivilegesResourceEntitlement entitlement,Certification cert, User reviewer) {
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
	

	private User mapToUserEntity(UserResponse userDto) {
		User user = new User();
		user.setExternalId(userDto.getExternalId());
		user.setName(userDto.getName());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setTenant(ServicesUtil.validateTenant(tenantRepo, userDto.getTenantName()));
		user.setOrganization(ServicesUtil.validateOrganization(orgRepo, userDto.getTenantName(), userDto.getOrganization()));
		user.setTenantName(userDto.getTenantName());
		return user;
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
		if(owner != null && !owner.trim().isEmpty()) {
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
	
	private User createAdminForCertification(Certification cert) {
		User user = new User();
		user.setExternalId("imadmin");
		user.setName("im");
		user.setFirstName("im");
		user.setLastName("");
		user.setEmail("imadmin@IdentEnv.com");
		user.setTenant(cert.getTenant());
		user.setOrganization(cert.getOrganization());
		user.setTenantName(cert.getTenantName());
		user.setCertificationName(cert.getName());
		user.setCertification(cert);
		user = userRepo.save(user);
		return user;
	}
	
	private Application createApplicationForCertification(Certification cert, User appOwner) {
		Application app = new Application();
		app.setExternalId("ad_test");
		app.setName("ad_test");
		app.setType("ActiveDirectory");
		app.setCertificationName(cert.getCertificationName());
		app.setTenant(cert.getTenant());
		app.setTenantName(cert.getTenantName());
		app.setOrganization(cert.getOrganization());
		app.setOwner(appOwner);
		app = appRepo.save(app);
		return app;
	}
	
}
