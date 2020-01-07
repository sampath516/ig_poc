package com.poc.ig.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.certification.entity.Certification.CertificationType;
import com.poc.ig.certification.test.dto.ApplicationDto;
import com.poc.ig.certification.test.dto.CreateCertificationRequest;
import com.poc.ig.certification.test.dto.GetReviewsByCertificationAndReviewerRequest.ReviewState;
import com.poc.ig.certification.test.dto.GetReviewsByCertificationAndReviewerResponse;
import com.poc.ig.certification.test.dto.OrganizationDto;
import com.poc.ig.certification.test.dto.ResourceResponse;
import com.poc.ig.certification.test.dto.ReviewRequest;
import com.poc.ig.certification.test.dto.ReviewRequest.Action;
import com.poc.ig.certification.test.dto.ReviewRequest.ActionType;
import com.poc.ig.certification.test.dto.ReviewResponse;
import com.poc.ig.certification.test.dto.UserDto;
import com.poc.ig.certification.test.dto.UserResourceEntitlementRequest;
import com.poc.ig.certification.test.dto.UserResourceEntitlementResponse;
import com.poc.ig.certification.util.CertificationTestUtil;
import com.poc.ig.certification.util.RepositoryClientUtil;



//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CertificationServiceTests {
	
	private RestTemplate restClient = new RestTemplate();
	private static String CERTIFICTION_TENANT_BASE_URI = "http://localhost:%d/ig/certification/v1/tenants";
	private String certificationTenantBaseUri;
	
	@BeforeEach
	public void init() {
		
		certificationTenantBaseUri = String.format(CERTIFICTION_TENANT_BASE_URI, port);
		CertificationTestUtil.setRestClient(restClient);
		CertificationTestUtil.setTenantBaseUri(certificationTenantBaseUri);
		
	}

//	@LocalServerPort   
	//	private int port;
			private int port=8084;
    
//	@Test
    public void testCertifications() {
    	
		String tenant1 = "Broadcom";
		String tenant1Desc = "Broadcom";
		
		String t1Org1ExtId = "ESD";
		String t1Org1Name = "ESD";
		String t1Org1Desc = "Enterprise Service Division";
		
		String t1App1ExtId = "Rally";
		String t1App1Name = "Rally";
		String t1App1Desc = "Rally Application";
		
	
		//Create a Tenant and Organization		
		CertificationTestUtil.createTenant(tenant1, tenant1Desc);
		OrganizationDto org1 = new OrganizationDto(t1Org1ExtId, t1Org1Name, t1Org1Desc);
		CertificationTestUtil.createOrganization(tenant1, org1);
		
		//Create Certification
		String certName = "TestCertification";
		String certDesc = "This is  test certification";
		String CertOwner = "USER-EID-2";
		CertificationType certType = CertificationType.USER_PREVILEGES; 
		CreateCertificationRequest request = new CreateCertificationRequest();
		request.setName(certName);
		request.setDescription(certDesc);
		request.setCertificationType(certType);
		request.setTenantName(tenant1);
		request.setOrganization(t1Org1ExtId);
		request.setOwner(CertOwner);
		CertificationTestUtil.createCertification(tenant1, request);	
		
		
		//Create Users
		List<UserDto> users = CertificationTestUtil.createUsers(tenant1, org1.getExternalId(), null,certName, 1,1);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		String manager1ExternalId =  users.get(0).getExternalId();
		users = CertificationTestUtil.createUsers(tenant1, org1.getExternalId(), manager1ExternalId,certName, 2,5);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 4);
		
		users = CertificationTestUtil.createUsers(tenant1, org1.getExternalId(), null,certName, 6,6);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		String manager2ExternalId =  users.get(0).getExternalId();
		users = CertificationTestUtil.createUsers(tenant1, org1.getExternalId(), manager2ExternalId, certName, 7,10);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 4);
		
		
		//Create an Application with resources		
		ApplicationDto app1 = new ApplicationDto(t1App1ExtId, t1App1Name, t1App1Desc, org1.getExternalId(),  users.get(1).getExternalId(),certName);
		CertificationTestUtil.createApplication(tenant1, app1);
		
		List<ResourceResponse> resources = CertificationTestUtil.createResources(tenant1, app1.getExternalId(),"USER-EID-5", certName, 1, 5);
		Assertions.assertTrue(resources.size()==5);
		
		resources = CertificationTestUtil.createResources(tenant1, app1.getExternalId(),"USER-EID-10", certName, 6, 10);
		Assertions.assertTrue(resources.size()==5);

    	
    }
	
	@Test
    public void testCertifications_v1() {
		
		String tenant1 = "Broadcom";
		String tenant1Desc = "Broadcom";
		
		String t1Org1ExtId = "ESD";
		String t1Org1Name = "ESD";
		String t1Org1Desc = "Enterprise Service Division";
		
	
		//Create a Tenant and Organization		
//		CertificationTestUtil.createTenant(tenant1, tenant1Desc);
//		OrganizationDto org1 = new OrganizationDto(t1Org1ExtId, t1Org1Name, t1Org1Desc);
//		CertificationTestUtil.createOrganization(tenant1, org1);
		
		//Create Certification
		String certName = "Cert-1";
		String certDesc = "This is  test certification";
		String CertOwner = "USER-EID-2";
		CertificationType certType = CertificationType.USER_PREVILEGES; 
		CreateCertificationRequest request = new CreateCertificationRequest();
		request.setName(certName);
		request.setDescription(certDesc);
		request.setCertificationType(certType);
		request.setTenantName(tenant1);
		request.setOrganization(t1Org1ExtId);
		request.setOwner(CertOwner);
		CertificationTestUtil.createCertification(tenant1, request);	
		
		//Get Entitlements from Repository
		UserResourceEntitlementResponse entitlementsResponse =  RepositoryClientUtil.getUserResourceEntitlements(tenant1, t1Org1ExtId);
		
		//Create reviews in Certification
		UserResourceEntitlementRequest userResourceEntitlementRequest = new UserResourceEntitlementRequest();
		userResourceEntitlementRequest.setTenantName(tenant1);
		userResourceEntitlementRequest.setCertification(certName);
		userResourceEntitlementRequest.getEntitlements().addAll(entitlementsResponse.getEntitlements());
		CertificationTestUtil.createUserResourceEntitlements(userResourceEntitlementRequest);
		
		//Get open reviews
		String reviewer = "USER-EID-1";
		GetReviewsByCertificationAndReviewerResponse  response =	CertificationTestUtil.getReviews(tenant1, certName, reviewer, ReviewState.OPEN);
		List<ReviewResponse>  reviews = response.getReviews();
		Assertions.assertEquals(3, response.getTotal());
		Assertions.assertEquals(3, reviews.size());
		
		// Approve/Reject reviews and submit
		ReviewRequest revReq = new ReviewRequest();
		for(ReviewResponse r: reviews) {
			if(r.getPrimaryEntity().getExternalId().equals("USER-EID-2") || r.getPrimaryEntity().getExternalId().equals("USER-EID-3")) {
				revReq.getReviewIds().add(r.getReviewId());
			}
		} 
		Action action = new Action();
		action.setComments("Approved from JUnit");
		action.setType(ActionType.APPROVE);
		
		revReq.setAction(action);
		
		CertificationTestUtil.updateReviews(tenant1, certName, reviewer, revReq);
		
		//Get open reviews
		reviewer = "USER-EID-5";
		response =	CertificationTestUtil.getReviews(tenant1, certName, reviewer, ReviewState.OPEN);
		reviews = response.getReviews();
		Assertions.assertEquals(4, response.getTotal());
		Assertions.assertEquals(4, reviews.size());
		
		// Approve/Reject reviews and submit
		revReq = new ReviewRequest();
		for(ReviewResponse r: reviews) {
			if(r.getPrimaryEntity().getExternalId().equals("USER-EID-2")) {
				revReq.getReviewIds().add(r.getReviewId());
			}
		} 
		action = new Action();
		action.setComments("Approved from JUnit");
		action.setType(ActionType.APPROVE);
		
		revReq.setAction(action);
		
		CertificationTestUtil.updateReviews(tenant1, certName, reviewer, revReq);
		
	}
}
