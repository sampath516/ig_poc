package com.poc.ig.repo.service;

import java.util.Arrays;
import java.util.List;

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

import com.poc.ig.repo.test.dto.Organization;
import com.poc.ig.repo.test.dto.OrganizationList;
import com.poc.ig.repo.test.dto.Tenant;
import com.poc.ig.repo.test.dto.TenantsList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TenantServiceTest {

	private RestTemplate restClient = new RestTemplate();
	private static String TENANT_BASE_URI = "http://localhost:%d/ig/repo/v1/tenants";
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

		Tenant getTenant = getTenant(createTenant.getName());
		Assert.assertEquals(createTenant.getName(), getTenant.getName());

		getTenant.setDescription("ABC Company Updated");
		Tenant updateTenant = updateTenant(getTenant);
		Assert.assertEquals(getTenant.getName(), updateTenant.getName());
		Assert.assertEquals(getTenant.getDescription(), updateTenant.getDescription());

		createTenant("MNO", "MNO Company");

		List<Tenant> tenants = listTenants();
		Assert.assertEquals(2, tenants.size());
		Assert.assertFalse(tenants.get(0).getName().equals(tenants.get(1).getName()));

		deleteTenant(tenants.get(0).getName());
		deleteTenant(tenants.get(1).getName());

		tenants = listTenants();
		Assert.assertEquals(0, tenants.size());

	}

	@Test
	public void testAddAndRemoveOrganizations() {

		Tenant abcTenant = createTenant("XYZ", "XYZ Company");
		Organization org1 = new Organization("XYZ_ORG1_KEY","XYZ_ORG1", "XYZ Organization One");
		org1 = createOrganization(abcTenant.getName(), org1);

		
		List<Organization> organizations = listOrganizations(abcTenant.getName());
		Assert.assertEquals(1, organizations.size());
		Assert.assertEquals("XYZ_ORG1_KEY", organizations.get(0).getExternalId());
		Assert.assertEquals("XYZ_ORG1", organizations.get(0).getName());
		Assert.assertEquals("XYZ Organization One", organizations.get(0).getDescription());	
		Assert.assertNotNull(organizations.get(0).getCreatedAt());
		Assert.assertNotNull(organizations.get(0).getUpdatedAt());
		
		
		
		Organization org2 = new Organization("XYZ_ORG2_KEY", "XYZ_ORG2", "XYZ Organization Two");
		org2 = createOrganization(abcTenant.getName(), org2);
		organizations = listOrganizations(abcTenant.getName());
		Assert.assertEquals(2, organizations.size());

		deleteOrganization(abcTenant.getName(), organizations.get(0).getExternalId());

		organizations = listOrganizations(abcTenant.getName());
		Assert.assertEquals(1, organizations.size());
		deleteTenant(abcTenant.getName());


	}

	private Tenant createTenant(String tenantName, String tenantDesc) {
		Tenant tenantIn = new Tenant(tenantName, tenantDesc);
		ResponseEntity<Tenant> createTenant = restClient.postForEntity(tenantBaseUri, tenantIn, Tenant.class);
		Assert.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assert.assertTrue(createTenant.hasBody());
		Assert.assertNotNull(createTenant.getBody().getId());
		return createTenant.getBody();
	}

	private Tenant getTenant(String tenantName) {
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

	private Tenant updateTenant(Tenant tenant) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenant.getName()).toString();
		ResponseEntity<Tenant> updateEntityTenant = restClient.exchange(uri, HttpMethod.PUT, new HttpEntity<>(tenant), Tenant.class);
		Assert.assertEquals(HttpStatus.OK, updateEntityTenant.getStatusCode());
		Assert.assertTrue(updateEntityTenant.hasBody());
		Assert.assertNotNull(updateEntityTenant.getBody());
		return updateEntityTenant.getBody();
	}

	private List<Tenant> listTenants() {
		HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(getHeaders());
		ResponseEntity<TenantsList> tenants = restClient.exchange(tenantBaseUri, HttpMethod.GET, tenantReq,
				TenantsList.class);
		Assert.assertEquals(HttpStatus.OK, tenants.getStatusCode());
		Assert.assertTrue(tenants.hasBody());
		Assert.assertNotNull(tenants.getBody());
		return tenants.getBody().getTenants();
	}

	private void deleteTenant(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).toString();
		HttpEntity<Tenant> deleteTenantReq = new HttpEntity<Tenant>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteTenantReq, Tenant.class);
	}

	private Organization createOrganization(String tenantName, Organization org) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations").toString();
		HttpEntity<Organization> orgReq = new HttpEntity<Organization>(org, getHeaders());
		ResponseEntity<Organization> orgOut = restClient.exchange(uri, HttpMethod.POST, orgReq, Organization.class);
		Assert.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assert.assertTrue(orgOut.hasBody());
		Assert.assertNotNull(orgOut.getBody());
		Assert.assertNotNull(orgOut.getBody().getId());
		return orgOut.getBody();
	}

	private void deleteOrganization(String tenantName, String orgExternalId) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations/").append(orgExternalId).toString();
		HttpEntity<Organization> deleteReq = new HttpEntity<Organization>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteReq, Organization.class);
	}

	private List<Organization> listOrganizations(String tenantName) {
		String uri = new StringBuilder(tenantBaseUri).append("/").append(tenantName).append("/organizations").toString();
		HttpEntity<Organization> listTenantsReq = new HttpEntity<Organization>(getHeaders());
		ResponseEntity<OrganizationList> organizations = restClient.exchange(uri, HttpMethod.GET, listTenantsReq, OrganizationList.class);
		Assert.assertEquals(HttpStatus.OK, organizations.getStatusCode());
		Assert.assertTrue(organizations.hasBody());
		Assert.assertNotNull(organizations.getBody());
		return organizations.getBody().getOrganizations();
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
