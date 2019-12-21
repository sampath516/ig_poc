package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.repo.test.dto.ApplicationDto; 
import com.poc.ig.repo.test.dto.OrganizationDto;
import com.poc.ig.repo.test.dto.ResourceDto;
import com.poc.ig.repo.test.dto.RoleDto;
import com.poc.ig.repo.test.dto.UserDto;
import com.poc.ig.repoutil.RepoTestUtil; 


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RepositoryServicesTest {

	private RestTemplate restClient = new RestTemplate();
	private static String TENANT_BASE_URI = "http://localhost:%d/ig/repo/v1/tenants";
	private String tenantBaseUri;

	@BeforeEach
	public void init() {
		tenantBaseUri = String.format(TENANT_BASE_URI, port);
		RepoTestUtil.setRestClient(restClient);
		RepoTestUtil.setTenantBaseUri(tenantBaseUri);
	}

	@LocalServerPort 
	private int port;
	
	@Test 
	public void testRepositoryCRUDOperations() {
		
		
		String tenant1 = "Broadcom";
		String tenant1Desc = "Broadcom";
		
		String t1Org1ExtId = "ESD";
		String t1Org1Name = "ESD";
		String t1Org1Desc = "Enterprise Service Division";
		
		String t1App1ExtId = "Rally";
		String t1App1Name = "Rally";
		String t1App1Desc = "Rally Application";
		
	
		//Create a Tenant and Organization		
		RepoTestUtil.createTenant(tenant1, tenant1Desc);
		OrganizationDto org1 = new OrganizationDto(t1Org1ExtId, t1Org1Name, t1Org1Desc);
		RepoTestUtil.createOrganization(tenant1, org1);
		
		//Create Users
		List<UserDto> users = RepoTestUtil.createUsers(tenant1, org1.getExternalId(), null, 1,1);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);	
		String managerExternalId =  users.get(0).getExternalId();
		users = RepoTestUtil.createUsers(tenant1, org1.getExternalId(), users.get(0).getExternalId(), 2,10);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 9);
		
		
		//Create an Application with resources		
		ApplicationDto app1 = new ApplicationDto(t1App1ExtId, t1App1Name, t1App1Desc, org1.getExternalId(),  users.get(1).getExternalId());
		RepoTestUtil.createApplication(tenant1, app1);
		
		List<ResourceDto> resources = RepoTestUtil.createResources(tenant1, app1.getExternalId(), 10);
		Assertions.assertTrue(resources.size()==10);

		
		//Create Roles
		List<RoleDto> roles = RepoTestUtil.createRoles(tenant1, org1.getExternalId(), users.get(2).getExternalId(), 1, 10);
		Assertions.assertTrue(roles.size()==10);
		//Assign Resources to Users
		
		//Assign Roles to Users
		
		//Link Resources to Roles
		
		//Unlink Resources from Roles
		
		//Unassign Roles from Users
		
		//Unassign Resources from Users
		
		//Delete Resources
		RepoTestUtil.deleteResources(tenant1, resources);;
		
		//Delete Applications
		RepoTestUtil.deleteApplication(tenant1, app1.getExternalId());
		
		//Delete Roles
		RepoTestUtil.deleteRoles(tenant1, roles);
		
		//Delete Users
		RepoTestUtil.deleteUsers(tenant1, users);
		List<UserDto> managers = new ArrayList<UserDto>();
		UserDto manager = new UserDto();
		manager.setExternalId(managerExternalId);
		managers.add(manager);
		RepoTestUtil.deleteUsers(tenant1, managers);
		
		//Delete Organization
		RepoTestUtil.deleteOrganization(tenant1, org1.getExternalId());
		
		//Delete Tenant
		RepoTestUtil.deleteTenant(tenant1);
	}

}
