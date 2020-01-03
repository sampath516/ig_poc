package com.poc.ig.certification.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.certification.dto.ApplicationResponse;
import com.poc.ig.certification.dto.UserResourceEntitlementResponse;
import com.poc.ig.certification.dto.UserResponse;
import com.poc.ig.certification.exception.InvalidUserException;
import com.poc.ig.certification.exception.UserResourceEntitlementsException;





@Component
public class RepositoryRestClient {

	@Autowired
	RestTemplate restTemplate;
	
	//private static final String  TENANT_SERVICE_BASE_URL = "http://localhost:8081/ig/repo/v1/tenants";
	
	private static final String  TENANT_SERVICE_BASE_URL = "http://repository-graph/ig/repo/v1/tenants";

	public UserResponse getUser(String tenantName, String userExternalId) {
		String uri = new StringBuilder(TENANT_SERVICE_BASE_URL).append("/").append(tenantName).append("/users/").append(userExternalId).toString();
		HttpEntity<Void> request = new HttpEntity<Void>(getHeaders());
		ResponseEntity<UserResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, UserResponse.class);
		if(response.getBody() == null) {
			throw new InvalidUserException("Invalid user(" + tenantName + ", " + userExternalId + ")");
		}
		return response.getBody();
	}
	
	public ApplicationResponse getApplication(String tenantName, String appExternalId) {
		String uri = new StringBuilder(TENANT_SERVICE_BASE_URL).append("/").append(tenantName).append("/applications/").append(appExternalId).toString();
		HttpEntity<Void> request = new HttpEntity<Void>(getHeaders());
		ResponseEntity<ApplicationResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request, ApplicationResponse.class);  
		if(response.getBody() == null) {
			throw new InvalidUserException("Invalid user(" + tenantName + ", " + appExternalId + ")");
		}
		return response.getBody();  
	}
	
	public UserResourceEntitlementResponse getUserResourceEntitlements(String tenantName, String organizationName) {

		String uri = new StringBuilder(TENANT_SERVICE_BASE_URL).append("/").append(tenantName).append("/organizations/")
				.append(organizationName).append("/users/resources").toString();
		HttpEntity<Void> request = new HttpEntity<Void>(getHeaders());
		ResponseEntity<UserResourceEntitlementResponse> response = restTemplate.exchange(uri, HttpMethod.GET, request,
				UserResourceEntitlementResponse.class);
		if (response.getBody() == null) {
			throw new UserResourceEntitlementsException(
					"Exception occured while fetching user resource entitlements( Tenant:  " + tenantName
							+ ", Organization:  " + organizationName + ")");
		}
		return response.getBody();
	}
	
	
	public void unlinkResourcesFromUser(String tenantName, String userExternalId, List<String> resourceExternalIds) {

		String uri = new StringBuilder(TENANT_SERVICE_BASE_URL).append("/").append(tenantName).append("/users").append("/")
				.append(userExternalId).append("/resources").toString();
		HttpEntity<List<String>> request = new HttpEntity<List<String>>(resourceExternalIds, getHeaders());
		restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
