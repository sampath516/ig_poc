package com.poc.ig.certification.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.certification.test.dto.ApplicationDto;
import com.poc.ig.certification.test.dto.CertificationResponse;
import com.poc.ig.certification.test.dto.CreateCertificationRequest;
import com.poc.ig.certification.test.dto.CreateResourceRequest;
import com.poc.ig.certification.test.dto.CreateResourceResponse;
import com.poc.ig.certification.test.dto.CreateRoleRequest;
import com.poc.ig.certification.test.dto.CreateRoleResponse;
import com.poc.ig.certification.test.dto.CreateUserRequest;
import com.poc.ig.certification.test.dto.CreateUserResponse;
import com.poc.ig.certification.test.dto.DeleteResourcesRequest;
import com.poc.ig.certification.test.dto.DeleteRolesRequest;
import com.poc.ig.certification.test.dto.DeleteUsersRequest;
import com.poc.ig.certification.test.dto.GetReviewsByCertificationAndReviewerRequest.ReviewState;
import com.poc.ig.certification.test.dto.GetReviewsByCertificationAndReviewerResponse;
import com.poc.ig.certification.test.dto.OrganizationDto;
import com.poc.ig.certification.test.dto.ResourceDto;
import com.poc.ig.certification.test.dto.ResourceResponse;
import com.poc.ig.certification.test.dto.ReviewRequest;
import com.poc.ig.certification.test.dto.RoleDto;
import com.poc.ig.certification.test.dto.RoleResponse;
import com.poc.ig.certification.test.dto.TenantDto;
import com.poc.ig.certification.test.dto.UserDto;
import com.poc.ig.certification.test.dto.UserResourceEntitlementRequest;


public class CertificationTestUtil {

	private static RestTemplate restClient;
	private static String tenantBaseUri;

	public static void setRestClient(RestTemplate restClientIn) {
		restClient = restClientIn;
	}

	public static void setTenantBaseUri(String tenantBaseUriIn) {
		tenantBaseUri = tenantBaseUriIn;
	}

	public static TenantDto createTenant(String tenantName, String tenantDesc) {
		TenantDto tenantIn = new TenantDto(tenantName, tenantDesc);
		ResponseEntity<TenantDto> createTenant = restClient.postForEntity(tenantBaseUri, tenantIn, TenantDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assertions.assertTrue(createTenant.hasBody());
		return createTenant.getBody();
	}

	public static OrganizationDto createOrganization(String tenantName, OrganizationDto org) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations")
				.toString();
		HttpEntity<OrganizationDto> orgReq = new HttpEntity<OrganizationDto>(org, getHeaders());
		ResponseEntity<OrganizationDto> orgOut = restClient.exchange(uri, HttpMethod.POST, orgReq,
				OrganizationDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assertions.assertTrue(orgOut.hasBody());
		Assertions.assertNotNull(orgOut.getBody());
		return orgOut.getBody();
	}

	public static ApplicationDto createApplication(String tenantName, ApplicationDto app) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/applications").toString();
		HttpEntity<ApplicationDto> appReq = new HttpEntity<ApplicationDto>(app, getHeaders());
		ResponseEntity<ApplicationDto> appOut = restClient.exchange(uri, HttpMethod.POST, appReq, ApplicationDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, appOut.getStatusCode());
		Assertions.assertTrue(appOut.hasBody());
		Assertions.assertNotNull(appOut.getBody());
		return appOut.getBody();
	}

	public static List<UserDto> createUsers(String tenantName, String organization, String manager,String certificationName, int start,
			int end) {

		List<UserDto> users = new ArrayList<UserDto>();
		String prefix = organization + "User";
		for (int i = start; i <= end; i++) {
			UserDto user = new UserDto("USER-EID-"+i, "USER-" + i, prefix + i + "FName",
					prefix + i + "LName", prefix + i + "Name@gmail.com", manager, organization, certificationName);
			users.add(user);
		}
		CreateUserRequest req = new CreateUserRequest();
		req.setUsers(users);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/").append(certificationName).append("/users").toString();
		HttpEntity<CreateUserRequest> userReq = new HttpEntity<CreateUserRequest>(req, getHeaders());
		ResponseEntity<CreateUserResponse> userOut = restClient.exchange(uri, HttpMethod.POST, userReq,
				CreateUserResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, userOut.getStatusCode());
		Assertions.assertTrue(userOut.hasBody());
		Assertions.assertNotNull(userOut.getBody());
		Assertions.assertNotNull(userOut.getBody().getUsers());
		return userOut.getBody().getUsers();
	}
	
	public static UserDto getUser(String tenantName, String userExternalId, String certificationName) {
		
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/").append(certificationName).append("/users/").append(userExternalId).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserDto> userReq = new HttpEntity<UserDto>(headers);
		ResponseEntity<UserDto> userResponse = restClient.exchange(uri, HttpMethod.GET, userReq, UserDto.class);
		Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
		Assertions.assertTrue(userResponse.hasBody());
		Assertions.assertNotNull(userResponse.getBody());
		Assertions.assertEquals(userExternalId, userResponse.getBody().getExternalId());		
		return userResponse.getBody();
	}

	public static List<RoleResponse> createRoles(String tenantName, String organization, String owner, int start, int end) {

		List<RoleDto> roles = new ArrayList<RoleDto>();
		String prefix = tenantName + "Role";
		for (int i = start; i <= end; i++) {
			RoleDto role = new RoleDto("ROLE-EID-"+i, "ROLE-" + i, prefix + i + "Desc", owner, organization);
			roles.add(role);
		}
		CreateRoleRequest req = new CreateRoleRequest();
		req.setRoles(roles);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles").toString();
		HttpEntity<CreateRoleRequest> roleReq = new HttpEntity<CreateRoleRequest>(req, getHeaders());
		ResponseEntity<CreateRoleResponse> roleOut = restClient.exchange(uri, HttpMethod.POST, roleReq,
				CreateRoleResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, roleOut.getStatusCode());
		Assertions.assertTrue(roleOut.hasBody());
		Assertions.assertNotNull(roleOut.getBody());
		Assertions.assertNotNull(roleOut.getBody().getRoles());
		return roleOut.getBody().getRoles();
	}
	
	public static RoleResponse getRole(String tenantName, String roleExternalId) {
		
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles/").append(roleExternalId).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RoleResponse> roleReq = new HttpEntity<RoleResponse>(headers);
		ResponseEntity<RoleResponse> roleResponse = restClient.exchange(uri, HttpMethod.GET, roleReq, RoleResponse.class);
		Assertions.assertEquals(HttpStatus.OK, roleResponse.getStatusCode());
		Assertions.assertTrue(roleResponse.hasBody());
		Assertions.assertNotNull(roleResponse.getBody());
		Assertions.assertEquals(roleExternalId, roleResponse.getBody().getExternalId());		
		return roleResponse.getBody();
	}

	
	
	public static List<ResourceResponse> createResources(String tenantName, String app,String owner, String certificationName, int start, int end) {

		List<ResourceDto> resources = new ArrayList<ResourceDto>();
		String prefix = app + "Resource";
		for (int i = start; i <= end; i++) {
			ResourceDto res = new ResourceDto("RES-EID-" + i, "RES-" + i, prefix + i + "Desc", owner, app, certificationName);
			resources.add(res);
		}
		CreateResourceRequest req = new CreateResourceRequest();
		req.setResources(resources);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/").append(certificationName).append("/resources").toString();
		HttpEntity<CreateResourceRequest> resReq = new HttpEntity<CreateResourceRequest>(req, getHeaders());
		ResponseEntity<CreateResourceResponse> resOut = restClient.exchange(uri, HttpMethod.POST, resReq,
				CreateResourceResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, resOut.getStatusCode());
		Assertions.assertTrue(resOut.hasBody());
		Assertions.assertNotNull(resOut.getBody());
		Assertions.assertNotNull(resOut.getBody().getResources());
		return resOut.getBody().getResources();
	}
	
	public static ResourceResponse getResource(String tenantName, String resourceExternalId, String certificationName) {
		
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/").append(certificationName).append("/resources/").append(resourceExternalId).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ResourceResponse> resReq = new HttpEntity<ResourceResponse>(headers);
		ResponseEntity<ResourceResponse> resRes = restClient.exchange(uri, HttpMethod.GET, resReq, ResourceResponse.class);
		Assertions.assertEquals(HttpStatus.OK, resRes.getStatusCode());
		Assertions.assertTrue(resRes.hasBody());
		Assertions.assertNotNull(resRes.getBody());
		Assertions.assertEquals(resourceExternalId, resRes.getBody().getExternalId());		
		return resRes.getBody();
	}
	
	public static void linkResourcesToUser(String tenantName, String userExternalId, List<String> resourceExternalIds) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").append("/")
				.append(userExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		ResponseEntity<UserDto> response = restClient.exchange(uri, HttpMethod.PUT, request, UserDto.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertTrue(response.hasBody());
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(userExternalId, response.getBody().getExternalId());
	}
	
	public static void unlinkResourcesFromUser(String tenantName, String userExternalId, List<String> resourceExternalIds) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").append("/")
				.append(userExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		ResponseEntity<Void> response = restClient.exchange(uri, HttpMethod.DELETE, request, Void.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	public static void linkResourcesToRole(String tenantName, String roleExternalId, List<String> resourceExternalIds) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles").append("/")
				.append(roleExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		ResponseEntity<RoleResponse> response = restClient.exchange(uri, HttpMethod.PUT, request, RoleResponse.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertTrue(response.hasBody());
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(roleExternalId, response.getBody().getExternalId());
	}
	
	public static void unlinkResourcesFromRole(String tenantName, String roleExternalId, List<String> resourceExternalIds) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles").append("/")
				.append(roleExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		ResponseEntity<Void> response = restClient.exchange(uri, HttpMethod.DELETE, request, Void.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	public static void linkResourcesToParentResource(String tenantName, String parentResourceExternalId, List<String> resourceExternalIds) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").append("/")
				.append(parentResourceExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		ResponseEntity<ResourceResponse> response = restClient.exchange(uri, HttpMethod.PUT, request, ResourceResponse.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertTrue(response.hasBody());
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(parentResourceExternalId, response.getBody().getExternalId());
	}
	
	public static void unlinkResourcesFromParentResource(String tenantName, String parentResourceExternalId, List<String> resourceExternalIds) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").append("/")
				.append(parentResourceExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		ResponseEntity<Void> response = restClient.exchange(uri, HttpMethod.DELETE, request, Void.class);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	public static void deleteResources(String tenantName, List<ResourceResponse> resources) {
		DeleteResourcesRequest delReq = new DeleteResourcesRequest();
		for (ResourceResponse res : resources) {
			delReq.getResources().add(res.getExternalId());
		}
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").toString();
		HttpEntity<DeleteResourcesRequest> delReqEntity = new HttpEntity<DeleteResourcesRequest>(delReq, getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, delReqEntity, Void.class);
	}

	public static void deleteUsers(String tenantName, List<UserDto> users) {
		DeleteUsersRequest delReq = new DeleteUsersRequest();
		for (UserDto user : users) {
			delReq.getUsers().add(user.getExternalId());
		}
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").toString();
		HttpEntity<DeleteUsersRequest> delReqEntity = new HttpEntity<DeleteUsersRequest>(delReq, getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, delReqEntity, Void.class);
	}

	public static void deleteRoles(String tenantName, List<RoleResponse> roles) {
		DeleteRolesRequest delReq = new DeleteRolesRequest();
		for (RoleResponse role : roles) {
			delReq.getRoles().add(role.getExternalId());
		}
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles").toString();
		HttpEntity<DeleteRolesRequest> delReqEntity = new HttpEntity<DeleteRolesRequest>(delReq, getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, delReqEntity, Void.class);
	}

	public static void deleteApplication(String tenantName, String appExternalId) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/applications/")
				.append(appExternalId).toString();
		HttpEntity<Void> delReqEntity = new HttpEntity<Void>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, delReqEntity, Void.class);

	}

	public static void deleteOrganization(String tenantName, String orgExternalId) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations/")
				.append(orgExternalId).toString();
		HttpEntity<OrganizationDto> deleteReq = new HttpEntity<OrganizationDto>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteReq, OrganizationDto.class);
	}

	public static void deleteTenant(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).toString();
		HttpEntity<TenantDto> deleteTenantReq = new HttpEntity<TenantDto>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteTenantReq, TenantDto.class);
	}
	
	
	public static CertificationResponse createCertification(String tenantName, CreateCertificationRequest certificationRequest) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/certifications")
				.toString();
		HttpEntity<CreateCertificationRequest> request = new HttpEntity<CreateCertificationRequest>(certificationRequest, getHeaders());
		ResponseEntity<CertificationResponse> orgOut = restClient.exchange(uri, HttpMethod.POST, request,	CertificationResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assertions.assertTrue(orgOut.hasBody());
		Assertions.assertNotNull(orgOut.getBody());
		return orgOut.getBody();
	}
	
	
	public static void createUserResourceEntitlements(UserResourceEntitlementRequest userResourceEntitlementRequest) {
		
		String uri = new StringBuilder(tenantBaseUri).append("/").append(userResourceEntitlementRequest.getTenantName())
				.append("/certifications/").append(userResourceEntitlementRequest.getCertification())
				.append("/user-res-entitlements")
				.toString();
		HttpEntity<UserResourceEntitlementRequest> request = new HttpEntity<UserResourceEntitlementRequest>(userResourceEntitlementRequest, getHeaders());
		ResponseEntity<Void> response = restClient.exchange(uri, HttpMethod.POST, request,	Void.class);
		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	public static GetReviewsByCertificationAndReviewerResponse getReviews(String tenantName, String certificationName,
			String reviewer, ReviewState reviewState) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName)
				.append("/certifications/").append(certificationName)
				.append("/reviewers/").append(reviewer).append("/reviews?").append("reviewState=").append(ReviewState.OPEN).toString();
		HttpEntity<Void> httpRequest = new HttpEntity<Void>(getHeaders());
		ResponseEntity<GetReviewsByCertificationAndReviewerResponse> httpResponse = restClient.exchange(uri, HttpMethod.GET, httpRequest, GetReviewsByCertificationAndReviewerResponse.class);
		Assertions.assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
		Assertions.assertTrue(httpResponse.hasBody());
		Assertions.assertNotNull(httpResponse.getBody());
		Assertions.assertEquals(tenantName,  httpResponse.getBody().getTenant());
		Assertions.assertEquals(certificationName,  httpResponse.getBody().getCertification());
		Assertions.assertEquals(reviewer,  httpResponse.getBody().getReviewer());
		return httpResponse.getBody();
	}
	
	public static void updateReviews(String tenantName, String certificationName,
			String reviewer, ReviewRequest request) {

		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName)
				.append("/certifications/").append(certificationName)
				.append("/reviewers/").append(reviewer).append("/reviews").toString();
		HttpEntity<ReviewRequest> httpRequest = new HttpEntity<ReviewRequest>(request, getHeaders());
		ResponseEntity<Void> httpResponse = restClient.exchange(uri, HttpMethod.PUT, httpRequest, Void.class);
		Assertions.assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}
	
	
	
	

	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
