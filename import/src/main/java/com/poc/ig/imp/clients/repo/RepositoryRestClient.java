package com.poc.ig.imp.clients.repo;

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

import com.poc.ig.imp.clients.repo.dto.Tenant;
import com.poc.ig.imp.clients.repo.dto.TenantList;

@Component
public class RepositoryRestClient {

	@Autowired
	RestTemplate restTemplate;
	
	private static final String  TENANT_SERVICE_BASE_URL = "http://repository/ig/repo/v1/tenants";

	public List<Tenant> getTenants() {
		HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(getHeaders());
		ResponseEntity<TenantList> tenantList = restTemplate.exchange(TENANT_SERVICE_BASE_URL, HttpMethod.GET, tenantReq,
				TenantList.class);
		return tenantList.getBody().getTenants();
	}
	
	public Tenant getTenant(String tenantName) {
			String uri = new StringBuilder(TENANT_SERVICE_BASE_URL).append("/").append(tenantName).toString();
			HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(getHeaders());
			ResponseEntity<Tenant> getTenant = restTemplate.exchange(uri, HttpMethod.GET, tenantReq, Tenant.class);
			return getTenant.getBody();

	}
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
