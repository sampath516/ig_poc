package com.poc.ig.connector.im.service;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class CertificationDataServiceTest {
	private RestTemplate restClient = new RestTemplate();
	private static String CERTIFICTION_TENANT_BASE_URI = "http://localhost:8085/ig/connector/im/v1/tenants/";
	
	
//	@Test
	public void test() {
		String tenantName = "xyz";
		String uri = new StringBuilder(CERTIFICTION_TENANT_BASE_URI).append(tenantName).append("/certification-data/import").toString();
		HttpEntity<Void> req = new HttpEntity<Void>(null, getHeaders());
		ResponseEntity<Void> res = restClient.exchange(uri, HttpMethod.POST, req, Void.class);
		Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

	}
	
	
	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
