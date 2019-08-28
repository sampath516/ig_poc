package com.poc.ig.repoutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.repo.dto.DeleteResourcesRequest;
import com.poc.ig.repo.test.dto.Application;
import com.poc.ig.repo.test.dto.CreateResourceRequest;
import com.poc.ig.repo.test.dto.CreateResourceResponse;
import com.poc.ig.repo.test.dto.CreateRoleRequest;
import com.poc.ig.repo.test.dto.CreateRoleResponse;
import com.poc.ig.repo.test.dto.CreateUserRequest;
import com.poc.ig.repo.test.dto.CreateUserResponse;
import com.poc.ig.repo.test.dto.DeleteRolesRequest;
import com.poc.ig.repo.test.dto.DeleteUsersRequest;
import com.poc.ig.repo.test.dto.Organization;
import com.poc.ig.repo.test.dto.Resource;
import com.poc.ig.repo.test.dto.Role;
import com.poc.ig.repo.test.dto.Tenant;
import com.poc.ig.repo.test.dto.User;

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
		Assert.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assert.assertTrue(createTenant.hasBody());
		Assert.assertNotNull(createTenant.getBody().getId());
		return createTenant.getBody();
	}

	public static Tenant getTenant(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(headers);
		ResponseEntity<Tenant> getTenant = restClient.exchange(uri, HttpMethod.GET, tenantReq, Tenant.class);
		Assert.assertEquals(HttpStatus.OK, getTenant.getStatusCode());
		Assert.assertTrue(getTenant.hasBody());
		Assert.assertNotNull(getTenant.getBody());
		return getTenant.getBody();
	}

	public static Organization createOrganization(String tenantName, Organization org) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations")
				.toString();
		HttpEntity<Organization> orgReq = new HttpEntity<Organization>(org, getHeaders());
		ResponseEntity<Organization> orgOut = restClient.exchange(uri, HttpMethod.POST, orgReq, Organization.class);
		Assert.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assert.assertTrue(orgOut.hasBody());
		Assert.assertNotNull(orgOut.getBody());
		Assert.assertNotNull(orgOut.getBody().getId());
		return orgOut.getBody();
	}

	public static Application createApplication(String tenantName, Application app) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/applications").toString();
		HttpEntity<Application> appReq = new HttpEntity<Application>(app, getHeaders());
		ResponseEntity<Application> appOut = restClient.exchange(uri, HttpMethod.POST, appReq, Application.class);
		Assert.assertEquals(HttpStatus.CREATED, appOut.getStatusCode());
		Assert.assertTrue(appOut.hasBody());
		Assert.assertNotNull(appOut.getBody());
		Assert.assertNotNull(appOut.getBody().getId());
		return appOut.getBody();
	}

	public static User createUser(String tenantName, User user) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").toString();
		HttpEntity<User> userReq = new HttpEntity<User>(user, getHeaders());
		ResponseEntity<User> userOut = restClient.exchange(uri, HttpMethod.POST, userReq, User.class);
		Assert.assertEquals(HttpStatus.CREATED, userOut.getStatusCode());
		Assert.assertTrue(userOut.hasBody());
		Assert.assertNotNull(userOut.getBody());
		Assert.assertNotNull(userOut.getBody().getId());
		return userOut.getBody();
	}

	public static List<User> createUsers(String tenantName, String organization, String manager, int start, int end) {

		List<User> users = new ArrayList<User>();
		String prefix = organization + "User";
		for (int i = start; i <= end; i++) {
			User user = new User(prefix + i + "ExternalId", prefix + i + "Name", prefix + i + "FName",
					prefix + i + "LName", prefix + i + "Name@gmail.com", manager, organization);
			users.add(user);
		}
		CreateUserRequest req = new CreateUserRequest();
		req.setUsers(users);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").toString();
		HttpEntity<CreateUserRequest> userReq = new HttpEntity<CreateUserRequest>(req, getHeaders());
		ResponseEntity<CreateUserResponse> userOut = restClient.exchange(uri, HttpMethod.POST, userReq,
				CreateUserResponse.class);
		Assert.assertEquals(HttpStatus.CREATED, userOut.getStatusCode());
		Assert.assertTrue(userOut.hasBody());
		Assert.assertNotNull(userOut.getBody());
		Assert.assertNotNull(userOut.getBody().getUsers());
		return userOut.getBody().getUsers();
	}

	public static List<Role> createRoles(String tenantName, String organization, String owner, int start, int end) {

		List<Role> roles = new ArrayList<Role>();
		String prefix = tenantName + "Role";
		for (int i = start; i <= end; i++) {
			Role role = new Role(prefix + i + "ExternalId", prefix + i + "Name", prefix + i + "Desc", owner, organization);
			roles.add(role);
		}
		CreateRoleRequest req = new CreateRoleRequest();
		req.setRoles(roles);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/roles").toString();
		HttpEntity<CreateRoleRequest> roleReq = new HttpEntity<CreateRoleRequest>(req, getHeaders());
		ResponseEntity<CreateRoleResponse> roleOut = restClient.exchange(uri, HttpMethod.POST, roleReq, CreateRoleResponse.class);
		Assert.assertEquals(HttpStatus.CREATED, roleOut.getStatusCode());
		Assert.assertTrue(roleOut.hasBody());
		Assert.assertNotNull(roleOut.getBody());
		Assert.assertNotNull(roleOut.getBody().getRoles());
		return roleOut.getBody().getRoles();
	}

	public static Resource createResource(String tenantName, Resource resource) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").toString();
		HttpEntity<Resource> resReq = new HttpEntity<Resource>(resource, getHeaders());
		ResponseEntity<Resource> resOut = restClient.exchange(uri, HttpMethod.POST, resReq, Resource.class);
		Assert.assertEquals(HttpStatus.CREATED, resOut.getStatusCode());
		Assert.assertTrue(resOut.hasBody());
		Assert.assertNotNull(resOut.getBody());
		Assert.assertNotNull(resOut.getBody());
		return resOut.getBody();
	}

	public static List<Resource> createResources(String tenantName, String app, int count) {

		List<Resource> resources = new ArrayList<Resource>();
		String prefix = app + "Resource";
		for (int i = 1; i <= count; i++) {
			Resource res = new Resource(prefix + i + "ExtId", prefix + i + "Name", prefix + i + "Desc", app);
			resources.add(res);
		}
		CreateResourceRequest req = new CreateResourceRequest();
		req.setResources(resources);
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").toString();
		HttpEntity<CreateResourceRequest> resReq = new HttpEntity<CreateResourceRequest>(req, getHeaders());
		ResponseEntity<CreateResourceResponse> resOut = restClient.exchange(uri, HttpMethod.POST, resReq,
				CreateResourceResponse.class);
		Assert.assertEquals(HttpStatus.CREATED, resOut.getStatusCode());
		Assert.assertTrue(resOut.hasBody());
		Assert.assertNotNull(resOut.getBody());
		Assert.assertNotNull(resOut.getBody().getResources());
		return resOut.getBody().getResources();
	}

	public static void deleteResources(String tenantName, List<Resource> resources) {
		DeleteResourcesRequest delReq = new DeleteResourcesRequest();
		for (Resource res : resources) {
			delReq.getResources().add(res.getExternalId());
		}
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/resources").toString();
		HttpEntity<DeleteResourcesRequest> delReqEntity = new HttpEntity<DeleteResourcesRequest>(delReq, getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, delReqEntity, Void.class);
	}

	public static void deleteUsers(String tenantName, List<User> users) {
		DeleteUsersRequest delReq = new DeleteUsersRequest();
		for (User user : users) {
			delReq.getUsers().add(user.getExternalId());
		}
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/users").toString();
		HttpEntity<DeleteUsersRequest> delReqEntity = new HttpEntity<DeleteUsersRequest>(delReq, getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, delReqEntity, Void.class);
	}
	
	public static void deleteRoles(String tenantName, List<Role> roles) {
		DeleteRolesRequest delReq = new DeleteRolesRequest();
		for (Role role : roles) {
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
		HttpEntity<Organization> deleteReq = new HttpEntity<Organization>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteReq, Organization.class);
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
