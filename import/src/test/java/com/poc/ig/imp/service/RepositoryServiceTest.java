package com.poc.ig.imp.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RepositoryServiceTest {
	private RestTemplate restClient = new RestTemplate();
	private static String TENANT_BASE_URI = "http://localhost:%d/ig/import/v1/tenants/";
	private String tenantBaseUri;

	@Before
	public void init() {
		tenantBaseUri = String.format(TENANT_BASE_URI, port);
	}

	@LocalServerPort
	private int port=8081;
	
	@Test
	public void testCreateRepository() {
		HttpEntity<Void> createRepoReq = new HttpEntity<Void>(getHeaders());
		String tenantName = "xyz";
		String uri = new StringBuilder(tenantBaseUri).append(tenantName).append("/").append("repository").toString();
		ResponseEntity<Void> createRepo = restClient.postForEntity(uri, createRepoReq, Void.class);
		Assert.assertEquals(HttpStatus.CREATED, createRepo.getStatusCode());
		
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
