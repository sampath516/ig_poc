package com.poc.ig.repo.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.repo.test.dto.Organization;
import com.poc.ig.repo.test.dto.OrganizationList;
import com.poc.ig.repo.test.dto.Tenant;
import com.poc.ig.repo.test.dto.TenantsList;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TenantServiceTest {

	private RestTemplate restClient = new RestTemplate();
	private static final String TENANT_BASE_URI = "http://localhost:8080/ig/repo/v1/tenants";

//	@Test
	public void testTenantCRUDOperations() {

		Tenant createTenant = createTenant("abc", "ABC Company");

		Tenant getTenant = getTenant(createTenant.getId());
		Assert.assertEquals(createTenant.getName(), getTenant.getName());

		getTenant.setName("XYZ");
		getTenant.setDescription("XYZ Company");
		Tenant updateTenant = updateTenant(getTenant);
		Assert.assertEquals(getTenant.getName(), updateTenant.getName());
		Assert.assertEquals(getTenant.getDescription(), updateTenant.getDescription());
		
		createTenant("MNO", "MNO Company");
		
		List<Tenant> tenants = listTenants();		
		Assert.assertEquals(2, tenants.size());
		Assert.assertEquals("XYZ", tenants.get(0).getName());
		Assert.assertEquals("MNO", tenants.get(1).getName());
		
		deleteTenant(tenants.get(0).getId());
		deleteTenant(tenants.get(1).getId());
		
		tenants = listTenants();	
		Assert.assertEquals(0, tenants.size());
		
		System.out.println("success");
	}
//	@Test
	public void testAddAndRemoveOrganizations() {
		
		Tenant abcTenant = createTenant("ABC", "ABC Company");
		Tenant xyzTenant = createTenant("XYZ", "XYZ Company");
		Organization org1 = new Organization("ABC_ORG1", "ABC Organization One");
		org1 =addOrganization(abcTenant.getId(), org1);
		
		Organization org2 = new Organization("ABC_ORG2", "ABC Organization Two");
		org2 = addOrganization(abcTenant.getId(), org2);
		
		List<Organization> organizations = listOrganizations(abcTenant.getId());
		Assert.assertEquals(2, organizations.size());		
		Assert.assertEquals("ABC_ORG1", organizations.get(0).getName());
		
		removeOrganization(abcTenant.getId(), organizations.get(0).getId());
		
		organizations = listOrganizations(abcTenant.getId());
        Assert.assertEquals(1, organizations.size());
		
		Assert.assertEquals("ABC_ORG2", organizations.get(0).getName());
		
		deleteTenant(abcTenant.getId());
		deleteTenant(xyzTenant.getId());
		
		
	}

	private Tenant createTenant(String tenantName, String tenantDesc) {
		Tenant tenantIn = new Tenant(tenantName, tenantDesc);
		ResponseEntity<Tenant> createTenant = restClient.postForEntity(TENANT_BASE_URI, tenantIn, Tenant.class);
		Assert.assertEquals(HttpStatus.CREATED, createTenant.getStatusCode());
		Assert.assertTrue(createTenant.hasBody());
		Assert.assertNotNull(createTenant.getBody().getId());
		return createTenant.getBody();
	}

	private Tenant getTenant(String tenantId) {
		String uri = new StringBuilder(TENANT_BASE_URI).append("/").append(tenantId).toString();
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
		ResponseEntity<Tenant> updateEntityTenant = restClient.exchange(TENANT_BASE_URI, HttpMethod.PUT,
				new HttpEntity<>(tenant), Tenant.class);
		Assert.assertEquals(HttpStatus.OK, updateEntityTenant.getStatusCode());
		Assert.assertTrue(updateEntityTenant.hasBody());
		Assert.assertNotNull(updateEntityTenant.getBody());
		return updateEntityTenant.getBody();
	}

	private List<Tenant> listTenants() {
		HttpEntity<Tenant> tenantReq = new HttpEntity<Tenant>(getHeaders());
		ResponseEntity<TenantsList> tenants = restClient.exchange(TENANT_BASE_URI, HttpMethod.GET, tenantReq, TenantsList.class);
		Assert.assertEquals(HttpStatus.OK, tenants.getStatusCode());
		Assert.assertTrue(tenants.hasBody());
		Assert.assertNotNull(tenants.getBody());
		return tenants.getBody().getTenants();
	}
	
	private void deleteTenant(String tenantId) {
		String uri = new StringBuilder(TENANT_BASE_URI).append("/").append(tenantId).toString();
		HttpEntity<Tenant> deleteTenantReq = new HttpEntity<Tenant>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteTenantReq, Tenant.class);
	}
	
	private Organization addOrganization(String tenantId, Organization org) {
		String uri = new StringBuilder(TENANT_BASE_URI).append("/").append(tenantId).append("/organizations").toString();
		HttpEntity<Organization> orgReq = new HttpEntity<Organization>(org, getHeaders());
		ResponseEntity<Organization> orgOut = restClient.exchange(uri, HttpMethod.POST, orgReq, Organization.class);
		Assert.assertEquals(HttpStatus.CREATED, orgOut.getStatusCode());
		Assert.assertTrue(orgOut.hasBody());
		Assert.assertNotNull(orgOut.getBody());
		Assert.assertNotNull(orgOut.getBody().getId());
		return orgOut.getBody();		
	}
	
	private void removeOrganization(String tenantId, String orgId) {
		String uri = new StringBuilder(TENANT_BASE_URI).append("/").append(tenantId).append("/organizations/").append(orgId).toString();
		HttpEntity<Organization> deleteReq = new HttpEntity<Organization>(getHeaders());
		restClient.exchange(uri, HttpMethod.DELETE, deleteReq, Organization.class);
	}	
	
	private List<Organization> listOrganizations(String tenantId) {
		String uri = new StringBuilder(TENANT_BASE_URI).append("/").append(tenantId).append("/organizations").toString();
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
