package com.poc.ig.gateway;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.gateway.test.dto.Tenant;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TenantServiceViaGatewayTest {

	private RestTemplate restClient = new RestTemplate();
	private static String TENANT_BASE_URI = "http://localhost:%d/repository/ig/repo/v1/tenants";
	private String tenantBaseUri;
	
	@Before
	public void init() {
		tenantBaseUri = String.format(TENANT_BASE_URI, port);
	}

	@LocalServerPort
	private int port;
	
	@Test 
	public void testTenantCRUDOperations() {
		tenantBaseUri = String.format(tenantBaseUri, port);
		Tenant createTenant = createTenant("ABC", "ABC Company");
		deleteTenant(createTenant.getName());
	}
	
	private Tenant createTenant(String tenantName, String tenantDesc) {
		Tenant tenantIn = new Tenant(tenantName, tenantDesc);
		ResponseEntity<Tenant> createTenant = restClient.postForEntity(tenantBaseUri, tenantIn, Tenant.class);
		Assert.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assert.assertTrue(createTenant.hasBody());
		Assert.assertNotNull(createTenant.getBody().getId());
		return createTenant.getBody();
	}
	
	private void deleteTenant(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).toString();
		HttpEntity<Tenant> deleteTenantReq = new HttpEntity<Tenant>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteTenantReq, Tenant.class);
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
