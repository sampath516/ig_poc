package com.poc.ig.certification.util;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import com.poc.ig.certification.exception.UserResourceEntitlementsException;
import com.poc.ig.certification.test.dto.UserResourceEntitlementResponse;

public class RepositoryClientUtil {
	private static RestTemplate restTemplate = new RestTemplate();
	private static String repositoryBaseUri = "http://localhost:8081/ig/repo/v1/tenants";

	public static UserResourceEntitlementResponse getUserResourceEntitlements(String tenantName, String organizationName) {

		String uri = new StringBuilder(repositoryBaseUri).append("/").append(tenantName).append("/organizations/")
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

	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
