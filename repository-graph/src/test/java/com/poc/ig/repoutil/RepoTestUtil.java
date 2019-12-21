package com.poc.ig.repoutil;

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


import com.poc.ig.repo.test.dto.ApplicationDto;
import com.poc.ig.repo.test.dto.CreateResourceRequest;
import com.poc.ig.repo.test.dto.CreateResourceResponse;
import com.poc.ig.repo.test.dto.CreateRoleRequest;
import com.poc.ig.repo.test.dto.CreateRoleResponse;
import com.poc.ig.repo.test.dto.CreateUserRequest;
import com.poc.ig.repo.test.dto.CreateUserResponse;
import com.poc.ig.repo.test.dto.DeleteResourcesRequest;
import com.poc.ig.repo.test.dto.DeleteRolesRequest;
import com.poc.ig.repo.test.dto.DeleteUsersRequest;
import com.poc.ig.repo.test.dto.OrganizationDto;
import com.poc.ig.repo.test.dto.ResourceDto;
import com.poc.ig.repo.test.dto.RoleDto;
import com.poc.ig.repo.test.dto.Tenant;
import com.poc.ig.repo.test.dto.UserDto;

public class RepoTestUtil { 

	private static RestTemplate restClient;
	private static String tenantBaseUri;

	public static void setRestClient(RestTemplate restClientIn) {
		restClient = restClientIn;
	}

	public static void setTenantBaseUri(String tenantBaseUriIn) {
		tenantBaseUri = tenantBaseUriIn;
	}

	public static Tenant createTenant(String tenantName, String tenantDesc) {
		Tenant tenantIn = new Tenant(tenantName, tenantDesc);
		ResponseEntity<Tenant> createTenant = restClient.postForEntity(tenantBaseUri, tenantIn, Tenant.class);
		Assertions.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assertions.assertTrue(createTenant.hasBody());
		Assertions.assertNotNull(createTenant.getBody().getId());
		return createTenant.getBody();
	}

	public static Tenant getTenant(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(headers);
		ResponseEntity<Tenant> getTenant = restClient.exchange(uri, HttpMethod.GET, tenantReq, Tenant.class);
		Assertions.assertEquals(HttpStatus.OK, getTenant.getStatusCode());
		Assertions.assertTrue(getTenant.hasBody());
		Assertions.assertNotNull(getTenant.getBody());
		return getTenant.getBody();
	}

	public static OrganizationDto createOrganization(String tenantName, OrganizationDto org) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations")
				.toString();
		HttpEntity<OrganizationDto> orgReq = new HttpEntity<OrganizationDto>(org, getHeaders());
		ResponseEntity<OrganizationDto> orgOut = restClient.exchange(uri, HttpMethod.POST, orgReq, OrganizationDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assertions.assertTrue(orgOut.hasBody());
		Assertions.assertNotNull(orgOut.getBody());
		Assertions.assertNotNull(orgOut.getBody().getId());
		return orgOut.getBody();
	}

	public static ApplicationDto createApplication(String tenantName, ApplicationDto app) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/applications").toString();
		HttpEntity<ApplicationDto> appReq = new HttpEntity<ApplicationDto>(app, getHeaders());
		ResponseEntity<ApplicationDto> appOut = restClient.exchange(uri, HttpMethod.POST, appReq, ApplicationDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, appOut.getStatusCode());
		Assertions.assertTrue(appOut.hasBody());
		Assertions.assertNotNull(appOut.getBody());
		Assertions.assertNotNull(appOut.getBody().getId());
		return appOut.getBody();
	}

	public static UserDto createUser(String tenantName, UserDto user) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").toString();
		HttpEntity<UserDto> userReq = new HttpEntity<UserDto>(user, getHeaders());
		ResponseEntity<UserDto> userOut = restClient.exchange(uri, HttpMethod.POST, userReq, UserDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, userOut.getStatusCode());
		Assertions.assertTrue(userOut.hasBody());
		Assertions.assertNotNull(userOut.getBody());
		Assertions.assertNotNull(userOut.getBody().getId());
		return userOut.getBody();
	}

	public static List<UserDto> createUsers(String tenantName, String organization, String manager, int start, int end) {

		List<UserDto> users = new ArrayList<UserDto>();
		String prefix = organization + "User";
		for (int i = start; i <= end; i++) {
//			User user = new User(prefix + i + "ExternalId", prefix + i + "Name", prefix + i + "FName",
//					prefix + i + "LName", prefix + i + "Name@gmail.com", manager, organization);
			UserDto user = new UserDto(prefix + i + "ExternalId", "User-" + i, prefix + i + "FName",
					prefix + i + "LName", prefix + i + "Name@gmail.com", manager, organization);
			users.add(user);
		}
		CreateUserRequest req = new CreateUserRequest();
		req.setUsers(users);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").toString();
		HttpEntity<CreateUserRequest> userReq = new HttpEntity<CreateUserRequest>(req, getHeaders());
		ResponseEntity<CreateUserResponse> userOut = restClient.exchange(uri, HttpMethod.POST, userReq,
				CreateUserResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, userOut.getStatusCode());
		Assertions.assertTrue(userOut.hasBody());
		Assertions.assertNotNull(userOut.getBody());
		Assertions.assertNotNull(userOut.getBody().getUsers());
		return userOut.getBody().getUsers();
	}

	public static List<RoleDto> createRoles(String tenantName, String organization, String owner, int start, int end) {

		List<RoleDto> roles = new ArrayList<RoleDto>();
		String prefix = tenantName + "Role";
		for (int i = start; i <= end; i++) {
		//	Role role = new Role(prefix + i + "ExternalId", prefix + i + "Name", prefix + i + "Desc", owner, organization);
			RoleDto role = new RoleDto(prefix + i + "ExternalId", "Role-" + i, prefix + i + "Desc", owner, organization);
			roles.add(role);
		}
		CreateRoleRequest req = new CreateRoleRequest();
		req.setRoles(roles);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles").toString();
		HttpEntity<CreateRoleRequest> roleReq = new HttpEntity<CreateRoleRequest>(req, getHeaders());
		ResponseEntity<CreateRoleResponse> roleOut = restClient.exchange(uri, HttpMethod.POST, roleReq, CreateRoleResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, roleOut.getStatusCode());
		Assertions.assertTrue(roleOut.hasBody());
		Assertions.assertNotNull(roleOut.getBody());
		Assertions.assertNotNull(roleOut.getBody().getRoles());
		return roleOut.getBody().getRoles();
	}

	public static ResourceDto createResource(String tenantName, ResourceDto resource) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").toString();
		HttpEntity<ResourceDto> resReq = new HttpEntity<ResourceDto>(resource, getHeaders());
		ResponseEntity<ResourceDto> resOut = restClient.exchange(uri, HttpMethod.POST, resReq, ResourceDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, resOut.getStatusCode());
		Assertions.assertTrue(resOut.hasBody());
		Assertions.assertNotNull(resOut.getBody());
		Assertions.assertNotNull(resOut.getBody());
		return resOut.getBody();
	}

	public static List<ResourceDto> createResources(String tenantName, String app, int count) {

		List<ResourceDto> resources = new ArrayList<ResourceDto>();
		String prefix = app + "Resource";
		for (int i = 1; i <= count; i++) {
//			Resource res = new Resource(prefix + i + "ExtId", prefix + i + "Name", prefix + i + "Desc", app);
			ResourceDto res = new ResourceDto(prefix + i + "ExtId", "RES-" + i, prefix + i + "Desc", app);
			resources.add(res);
		}
		CreateResourceRequest req = new CreateResourceRequest();
		req.setResources(resources);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").toString();
		HttpEntity<CreateResourceRequest> resReq = new HttpEntity<CreateResourceRequest>(req, getHeaders());
		ResponseEntity<CreateResourceResponse> resOut = restClient.exchange(uri, HttpMethod.POST, resReq, CreateResourceResponse.class);
		Assertions.assertEquals(HttpStatus.CREATED, resOut.getStatusCode());
		Assertions.assertTrue(resOut.hasBody());
		Assertions.assertNotNull(resOut.getBody());
		Assertions.assertNotNull(resOut.getBody().getResources());
		return resOut.getBody().getResources();
	}

	public static void deleteResources(String tenantName, List<ResourceDto> resources) {
		DeleteResourcesRequest delReq = new DeleteResourcesRequest();
		for (ResourceDto res : resources) {
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
	
	public static void deleteRoles(String tenantName, List<RoleDto> roles) {
		DeleteRolesRequest delReq = new DeleteRolesRequest();
		for (RoleDto role : roles) {
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
		HttpEntity<Tenant> deleteTenantReq = new HttpEntity<Tenant>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteTenantReq, Tenant.class);
	}

	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
