package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.repo.test.dto.Application;
import com.poc.ig.repo.test.dto.Organization;
import com.poc.ig.repo.test.dto.Resource;
import com.poc.ig.repo.test.dto.Role;
import com.poc.ig.repo.test.dto.User;
import com.poc.ig.repoutil.RepoTestUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RepositoryServicesTest {

	private RestTemplate restClient = new RestTemplate();
	private static String TENANT_BASE_URI = "http://localhost:%d/ig/repo/v1/tenants";
	private String tenantBaseUri;

	@Before
	public void init() {
		tenantBaseUri = String.format(TENANT_BASE_URI, port);
		RepoTestUtil.setRestClient(restClient);
		RepoTestUtil.setTenantBaseUri(tenantBaseUri);
	}

	@LocalServerPort
	private int port;
	
	@Test
	public void testRepositoryCRUDOperations() {
		
		
		String tenant1 = "tenant1";
		String tenant1Desc = "tenant1Desc";
		
		String t1Org1ExtId = "t1Org1ExtId";
		String t1Org1Name = "t1Org1Name";
		String t1Org1Desc = "t1Org1Desc";
		
		String t1App1ExtId = "t1App1ExtId";
		String t1App1Name = "t1App1Name";
		String t1App1Desc = "t1App1Desc";
		
	
		//Create a Tenant and Organization		
		RepoTestUtil.createTenant(tenant1, tenant1Desc);
		Organization org1 = new Organization(t1Org1ExtId, t1Org1Name, t1Org1Desc);
		RepoTestUtil.createOrganization(tenant1, org1);
		
		//Create Users
		List<User> users = RepoTestUtil.createUsers(tenant1, org1.getExternalId(), null, 1,1);
		Assert.assertNotNull(users);
		Assert.assertTrue(users.size() == 1);	
		String managerExternalId =  users.get(0).getExternalId();
		users = RepoTestUtil.createUsers(tenant1, org1.getExternalId(), users.get(0).getExternalId(), 2,100);
		Assert.assertNotNull(users);
		Assert.assertTrue(users.size() == 99);
		
		
		//Create an Application with resources		
		Application app1 = new Application(t1App1ExtId, t1App1Name, t1App1Desc, org1.getExternalId(),  users.get(1).getExternalId());
		RepoTestUtil.createApplication(tenant1, app1);
		
		List<Resource> resources = RepoTestUtil.createResources(tenant1, app1.getExternalId(), 100);
		Assert.assertTrue(resources.size()==100);

		
		//Create Roles
		List<Role> roles = RepoTestUtil.createRoles(tenant1, org1.getExternalId(), users.get(2).getExternalId(), 1, 100);
		Assert.assertTrue(roles.size()==100);
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
		List<User> managers = new ArrayList<User>();
		User manager = new User();
		manager.setExternalId(managerExternalId);
		managers.add(manager);
		RepoTestUtil.deleteUsers(tenant1, managers);
		
		//Delete Organization
		RepoTestUtil.deleteOrganization(tenant1, org1.getExternalId());
		
		//Delete Tenant
		RepoTestUtil.deleteTenant(tenant1);
	}

}
