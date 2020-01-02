package com.poc.ig.repo.service;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.repo.test.dto.OrganizationDto;
import com.poc.ig.repo.test.dto.OrganizationList;
import com.poc.ig.repo.test.dto.Tenant;
import com.poc.ig.repo.test.dto.TenantsList;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TenantServiceTest {

	private RestTemplate restClient = new RestTemplate();
	private static String TENANT_BASE_URI = "http://localhost:%d/ig/repo/v1/tenants";
	//private static String TENANT_BASE_URI = "http://localhost:8080/ig/repo/v1/tenants";
	private String tenantBaseUri;

	@BeforeEach
	public void init() {
		tenantBaseUri = String.format(TENANT_BASE_URI, port);
	}

	@LocalServerPort
	private int port;
	//private int port=8080;

	@Test
	public void testTenantCRUDOperations() {

		tenantBaseUri = String.format(tenantBaseUri, port);
		Tenant createTenant = createTenant("ABC", "ABC Company");

		Tenant getTenant = getTenant(createTenant.getName());
		Assertions.assertEquals(createTenant.getName(), getTenant.getName());

		getTenant.setDescription("ABC Company Updated");
		Tenant updateTenant = updateTenant(getTenant);
		Assertions.assertEquals(getTenant.getName(), updateTenant.getName());
		Assertions.assertEquals(getTenant.getDescription(), updateTenant.getDescription());

		createTenant("MNO", "MNO Company");

		List<Tenant> tenants = listTenants();
		Assertions.assertEquals(2, tenants.size());
		Assertions.assertFalse(tenants.get(0).getName().equals(tenants.get(1).getName()));

		deleteTenant(tenants.get(0).getName());
		deleteTenant(tenants.get(1).getName());

		tenants = listTenants();
		Assertions.assertEquals(0, tenants.size());

	}

	@Test
	public void testAddAndRemoveOrganizations() {

		Tenant abcTenant = createTenant("XYZ", "XYZ Company");
		OrganizationDto org1 = new OrganizationDto("XYZ_ORG1_KEY","XYZ_ORG1", "XYZ Organization One");
		org1 = createOrganization(abcTenant.getName(), org1);

		
		List<OrganizationDto> organizations = listOrganizations(abcTenant.getName());
		Assertions.assertEquals(1, organizations.size());
		Assertions.assertEquals("XYZ_ORG1_KEY", organizations.get(0).getExternalId());
		Assertions.assertEquals("XYZ_ORG1", organizations.get(0).getName());
		Assertions.assertEquals("XYZ Organization One", organizations.get(0).getDescription());	
		Assertions.assertNotNull(organizations.get(0).getCreatedAt());
		Assertions.assertNotNull(organizations.get(0).getUpdatedAt());
		
		
		
		OrganizationDto org2 = new OrganizationDto("XYZ_ORG2_KEY", "XYZ_ORG2", "XYZ Organization Two");
		org2 = createOrganization(abcTenant.getName(), org2);
		organizations = listOrganizations(abcTenant.getName());
		Assertions.assertEquals(2, organizations.size());

		deleteOrganization(abcTenant.getName(), organizations.get(0).getExternalId());

		organizations = listOrganizations(abcTenant.getName());
		Assertions.assertEquals(1, organizations.size());
		deleteOrganization(abcTenant.getName(), organizations.get(0).getExternalId());
		deleteTenant(abcTenant.getName());


	}

	private Tenant createTenant(String tenantName, String tenantDesc) {
		Tenant tenantIn = new Tenant(tenantName, tenantDesc);
		ResponseEntity<Tenant> createTenant = restClient.postForEntity(tenantBaseUri, tenantIn, Tenant.class);
		Assertions.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assertions.assertTrue(createTenant.hasBody());
		Assertions.assertNotNull(createTenant.getBody().getId());
		return createTenant.getBody();
	}

	private Tenant getTenant(String tenantName) {
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

	private Tenant updateTenant(Tenant tenant) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenant.getName()).toString();
		ResponseEntity<Tenant> updateEntityTenant = restClient.exchange(uri, HttpMethod.PUT, new HttpEntity<>(tenant), Tenant.class);
		Assertions.assertEquals(HttpStatus.OK, updateEntityTenant.getStatusCode());
		Assertions.assertTrue(updateEntityTenant.hasBody());
		Assertions.assertNotNull(updateEntityTenant.getBody());
		return updateEntityTenant.getBody();
	}

	private List<Tenant> listTenants() {
		HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(getHeaders());
		ResponseEntity<TenantsList> tenants = restClient.exchange(tenantBaseUri, HttpMethod.GET, tenantReq,
				TenantsList.class);
		Assertions.assertEquals(HttpStatus.OK, tenants.getStatusCode());
		Assertions.assertTrue(tenants.hasBody());
		Assertions.assertNotNull(tenants.getBody());
		return tenants.getBody().getTenants();
	}

	private void deleteTenant(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).toString();
		HttpEntity<Tenant> deleteTenantReq = new HttpEntity<Tenant>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteTenantReq, Tenant.class);
	}

	private OrganizationDto createOrganization(String tenantName, OrganizationDto org) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations").toString();
		HttpEntity<OrganizationDto> orgReq = new HttpEntity<OrganizationDto>(org, getHeaders());
		ResponseEntity<OrganizationDto> orgOut = restClient.exchange(uri, HttpMethod.POST, orgReq, OrganizationDto.class);
		Assertions.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assertions.assertTrue(orgOut.hasBody());
		Assertions.assertNotNull(orgOut.getBody());
		Assertions.assertNotNull(orgOut.getBody().getId());
		return orgOut.getBody();
	}

	private void deleteOrganization(String tenantName, String orgExternalId) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations/").append(orgExternalId).toString();
		HttpEntity<OrganizationDto> deleteReq = new HttpEntity<OrganizationDto>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteReq, OrganizationDto.class);
	}

	private List<OrganizationDto> listOrganizations(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations").toString();
		HttpEntity<OrganizationDto> listTenantsReq = new HttpEntity<OrganizationDto>(getHeaders());
		ResponseEntity<OrganizationList> organizations = restClient.exchange(uri, HttpMethod.GET, listTenantsReq, OrganizationList.class);
		Assertions.assertEquals(HttpStatus.OK, organizations.getStatusCode());
		Assertions.assertTrue(organizations.hasBody());
		Assertions.assertNotNull(organizations.getBody());
		return organizations.getBody().getOrganizations();
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
