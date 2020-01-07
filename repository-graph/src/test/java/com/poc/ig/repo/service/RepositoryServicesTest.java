package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.poc.ig.repo.test.dto.ApplicationDto;
import com.poc.ig.repo.test.dto.OrganizationDto;
import com.poc.ig.repo.test.dto.ResourceResponse;
import com.poc.ig.repo.test.dto.RoleResponse;
import com.poc.ig.repo.test.dto.UserDto;
import com.poc.ig.repo.test.dto.UserResourceEntitlement;
import com.poc.ig.repoutil.RepoTestUtil; 


//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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

 //@LocalServerPort 
 	private int port=8081;  
 	// 	   private int port;
	
//	@Test
    public void prepareCertificationDataAndTestEntitlements() {
		String tenantBroadcom = "Broadcom";
		String tenantBroadcomDesc = "Broadcom";
		
		String tenantBroadcomOrgESDExtId = "ESD";
		String tenantBroadcomOrgESDName = "ESD";
		String tenantBroadcomOrgESDDesc = "Enterprise Service Division";
		
		String appRallyExtId = "Rally";
		String appRallyName = "Rally";
		String appRallyDesc = "Rally Application";
		
		String appJenkinsExtId = "Jenkins";
		String appJenkinsName = "Jenkins";
		String appJenkinsDesc = "Jenkins Application";
		
	
		//Create a Tenant and Organization		
		RepoTestUtil.createTenant(tenantBroadcom, tenantBroadcomDesc);
		OrganizationDto org1 = new OrganizationDto(tenantBroadcomOrgESDExtId, tenantBroadcomOrgESDName, tenantBroadcomOrgESDDesc);
		RepoTestUtil.createOrganization(tenantBroadcom, org1);
		
		//Create Users
		
		List<UserDto> users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), null, 1,1);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		//USER-EID-1
		String manager1ExternalId =  users.get(0).getExternalId();
		
		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), null, 5,5);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		//USER-EID-5
		String owner1ExternalId =  users.get(0).getExternalId();
		
		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), null, 6,6);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		//USER-EID-6
		String manager2ExternalId =  users.get(0).getExternalId();
		
		
		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), null, 10,10);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		//USER-EID-10
		String owner2ExternalId =  users.get(0).getExternalId();
				
		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), manager1ExternalId, 2,4);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 3);
		

		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), manager2ExternalId, 7,9);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 3);
		
		
		//Create an Application with resources	
		//Rally, //USER-EID-5
		ApplicationDto appRally = new ApplicationDto(appRallyExtId, appRallyName, appRallyDesc, org1.getExternalId(),  owner1ExternalId);
		RepoTestUtil.createApplication(tenantBroadcom, appRally);
		
		//Jenkins, //USER-EID-10
		ApplicationDto appJenkins = new ApplicationDto(appJenkinsExtId, appJenkinsName, appJenkinsDesc, org1.getExternalId(),  owner2ExternalId);
		RepoTestUtil.createApplication(tenantBroadcom, appJenkins);
		
		List<ResourceResponse> resources = RepoTestUtil.createResources(tenantBroadcom, appRally.getExternalId(),owner1ExternalId, 1, 5);
		Assertions.assertTrue(resources.size()==5);
		
		resources = RepoTestUtil.createResources(tenantBroadcom, appJenkins.getExternalId(),owner2ExternalId, 6, 10);
		Assertions.assertTrue(resources.size()==5);
		
		//Assign Resources to Users
		String resExternalId = "RES-EID-1";
		List<String> userExternalIds = new ArrayList<String>();
		userExternalIds.add("USER-EID-1");
		userExternalIds.add("USER-EID-2");
		userExternalIds.add("USER-EID-3");
		userExternalIds.add("USER-EID-4");
		RepoTestUtil.linkUsersToResource(tenantBroadcom, resExternalId, userExternalIds);
		
		resExternalId = "RES-EID-6";
		userExternalIds = new ArrayList<String>();
		userExternalIds.add("USER-EID-7");
		userExternalIds.add("USER-EID-8");
		userExternalIds.add("USER-EID-9");
		RepoTestUtil.linkUsersToResource(tenantBroadcom, resExternalId, userExternalIds);
		
		//Verifying Entitlements
		List<UserResourceEntitlement> entitlements = RepoTestUtil.getUserResourceEntitlementsByTenantNameAndOrgName(tenantBroadcom, tenantBroadcomOrgESDExtId); 
		Assertions.assertEquals(7, entitlements.size());
		Assertions.assertNotNull(entitlements.get(0));
		Assertions.assertEquals(tenantBroadcom, entitlements.get(0).getTenantName());
		Assertions.assertEquals(tenantBroadcomOrgESDExtId, entitlements.get(0).getOrganization());
		
		
		//Cleanup
		
		//Delete Resources
		String[] resourceExtIds = new String[] {"RES-EID-1","RES-EID-2","RES-EID-3","RES-EID-4","RES-EID-5","RES-EID-6","RES-EID-7","RES-EID-8","RES-EID-9","RES-EID-10"};
		RepoTestUtil.deleteResources(tenantBroadcom, resourceExtIds);;
		
		//Delete Applications
		RepoTestUtil.deleteApplication(tenantBroadcom, appRallyExtId);
		RepoTestUtil.deleteApplication(tenantBroadcom, appJenkinsExtId);
		
		//Delete Roles

		
		//Delete Users
		String[] userExtIds = new String[] {"USER-EID-1","USER-EID-2","USER-EID-3","USER-EID-4","USER-EID-5","USER-EID-6","USER-EID-7","USER-EID-8","USER-EID-9","USER-EID-10"};
		RepoTestUtil.deleteUsers(tenantBroadcom, userExtIds); 

		//Delete Organization
		RepoTestUtil.deleteOrganization(tenantBroadcom, tenantBroadcomOrgESDExtId);
		
		//Delete Tenant
		RepoTestUtil.deleteTenant(tenantBroadcom);

    }
	
	@Test
    public void prepareCertificationDataAndTestEntitlements_v1() {
		String tenantBroadcom = "Broadcom";
		String tenantBroadcomDesc = "Broadcom";
		
		String tenantBroadcomOrgESDExtId = "ESD";
		String tenantBroadcomOrgESDName = "ESD";
		String tenantBroadcomOrgESDDesc = "Enterprise Service Division";
		
		String appRallyExtId = "Rally";
		String appRallyName = "Rally";
		String appRallyDesc = "Rally Application";
		
	
		//Create a Tenant and Organization		
		RepoTestUtil.createTenant(tenantBroadcom, tenantBroadcomDesc);
		OrganizationDto org1 = new OrganizationDto(tenantBroadcomOrgESDExtId, tenantBroadcomOrgESDName, tenantBroadcomOrgESDDesc);
		RepoTestUtil.createOrganization(tenantBroadcom, org1);
		
		//Create Users
		
		List<UserDto> users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), null, 1,1);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		//USER-EID-1
		String manager1ExternalId =  users.get(0).getExternalId();
		
		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), null, 5,5);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 1);
		//USER-EID-5
		String owner1ExternalId =  users.get(0).getExternalId();
		
			
		users = RepoTestUtil.createUsers(tenantBroadcom, org1.getExternalId(), manager1ExternalId, 2,4);
		Assertions.assertNotNull(users);
		Assertions.assertTrue(users.size() == 3);
		
		
		//Create an Application with resources	
		//Rally, //USER-EID-5
		ApplicationDto appRally = new ApplicationDto(appRallyExtId, appRallyName, appRallyDesc, org1.getExternalId(),  owner1ExternalId);
		RepoTestUtil.createApplication(tenantBroadcom, appRally);
		
		
		List<ResourceResponse> resources = RepoTestUtil.createResources(tenantBroadcom, appRally.getExternalId(),owner1ExternalId, 1, 5);
		Assertions.assertTrue(resources.size()==5);
		
		//Assign Resources to Users
		String resExternalId = "RES-EID-1";
		List<String> userExternalIds = new ArrayList<String>();
		userExternalIds.add("USER-EID-1");
		userExternalIds.add("USER-EID-2");
		userExternalIds.add("USER-EID-3");
		userExternalIds.add("USER-EID-4");
		RepoTestUtil.linkUsersToResource(tenantBroadcom, resExternalId, userExternalIds);
		
		
		//Verifying Entitlements
		List<UserResourceEntitlement> entitlements = RepoTestUtil.getUserResourceEntitlementsByTenantNameAndOrgName(tenantBroadcom, tenantBroadcomOrgESDExtId); 
		Assertions.assertEquals(4, entitlements.size());
		Assertions.assertNotNull(entitlements.get(0));
		Assertions.assertEquals(tenantBroadcom, entitlements.get(0).getTenantName());
		Assertions.assertEquals(tenantBroadcomOrgESDExtId, entitlements.get(0).getOrganization());
		
		
		//Cleanup
		
		//Delete Resources
		String[] resourceExtIds = new String[] {"RES-EID-1","RES-EID-2","RES-EID-3","RES-EID-4","RES-EID-5"};
		RepoTestUtil.deleteResources(tenantBroadcom, resourceExtIds);;
		
		//Delete Applications
		RepoTestUtil.deleteApplication(tenantBroadcom, appRallyExtId);
		
		//Delete Roles

		
		//Delete Users
		String[] userExtIds = new String[] {"USER-EID-1","USER-EID-2","USER-EID-3","USER-EID-4","USER-EID-5"};
		RepoTestUtil.deleteUsers(tenantBroadcom, userExtIds); 

		//Delete Organization
		RepoTestUtil.deleteOrganization(tenantBroadcom, tenantBroadcomOrgESDExtId);
		
		//Delete Tenant
		RepoTestUtil.deleteTenant(tenantBroadcom); 

    }
    
    
// @Test  
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
		
		List<ResourceResponse> resources = RepoTestUtil.createResources(tenant1, app1.getExternalId(),"USER-EID-1", 1, 10);
		Assertions.assertTrue(resources.size()==10);
		
		//Link Resources to Parent Resource
		String parentResourceExternalId = "RES-EID-2";
		List<String> chieldResExternalIds = new ArrayList<String>();
		chieldResExternalIds.add("RES-EID-3");
		chieldResExternalIds.add("RES-EID-4");
		RepoTestUtil.linkResourcesToParentResource(tenant1, parentResourceExternalId, chieldResExternalIds);
		ResourceResponse resource = RepoTestUtil.getResource(tenant1, parentResourceExternalId);
		
		Assertions.assertEquals(chieldResExternalIds.size(), resource.getSubResources().size());
		Assertions.assertTrue(resource.getSubResources().get(0).getExternalId() != resource.getSubResources().get(1).getExternalId());
		Assertions.assertTrue(resource.getSubResources().get(0).getExternalId().equals(chieldResExternalIds.get(0)) || resource.getSubResources().get(0).getExternalId().equals(chieldResExternalIds.get(1)) );

		
		//Create Roles
		List<RoleResponse> roles = RepoTestUtil.createRoles(tenant1, org1.getExternalId(), users.get(2).getExternalId(), 1, 10);
		Assertions.assertTrue(roles.size()==10);
		
		//Link Roles to Parent Role
		String parentRoleExternalId = "ROLE-EID-2";
		List<String> chieldRoleExternalIds = new ArrayList<String>();
		chieldRoleExternalIds.add("ROLE-EID-3");
		chieldRoleExternalIds.add("ROLE-EID-4");
		RepoTestUtil.linkRolesToParentRole(tenant1, parentRoleExternalId, chieldRoleExternalIds);
		RoleResponse role = RepoTestUtil.getRole(tenant1, parentRoleExternalId);
		
		Assertions.assertEquals(chieldRoleExternalIds.size(), role.getSubRoles().size());
		Assertions.assertTrue(role.getSubRoles().get(0).getExternalId() != role.getSubRoles().get(1).getExternalId());
		Assertions.assertTrue(role.getSubRoles().get(0).getExternalId().equals(chieldRoleExternalIds.get(0)) || role.getSubRoles().get(0).getExternalId().equals(chieldRoleExternalIds.get(1)) );

		
		
		//Assign Roles to Users
		String userExternalId = "USER-EID-3";
		List<String> roleExternalIds = new ArrayList<String>();
		roleExternalIds.add("ROLE-EID-3");
		roleExternalIds.add("ROLE-EID-4");
		RepoTestUtil.linkRolesToUser(tenant1, userExternalId, roleExternalIds);
		UserDto userDto = RepoTestUtil.getUser(tenant1, userExternalId);
		
		Assertions.assertEquals(roleExternalIds.size(), userDto.getRoles().size());
		Assertions.assertTrue(userDto.getRoles().get(0).getExternalId() != userDto.getRoles().get(1).getExternalId());
		Assertions.assertTrue(userDto.getRoles().get(0).getExternalId().equals(roleExternalIds.get(0)) || userDto.getRoles().get(0).getExternalId().equals(roleExternalIds.get(1)) );
		
		//Assign Resources to Users
		List<String> resExternalIds = new ArrayList<String>();
		resExternalIds.add("RES-EID-3");
		resExternalIds.add("RES-EID-4");
		RepoTestUtil.linkResourcesToUser(tenant1, userExternalId, resExternalIds);
		userDto = RepoTestUtil.getUser(tenant1, userExternalId);
		
		Assertions.assertEquals(resExternalIds.size(), userDto.getResources().size());
		Assertions.assertTrue(userDto.getResources().get(0).getExternalId() != userDto.getResources().get(1).getExternalId());
		Assertions.assertTrue(userDto.getResources().get(0).getExternalId().equals(resExternalIds.get(0)) || userDto.getResources().get(0).getExternalId().equals(resExternalIds.get(1)) );

		
		
		//Link Resources to Roles
		String roleExternalId = "ROLE-EID-4";
		resExternalIds = new ArrayList<String>();
		resExternalIds.add("RES-EID-6");
		resExternalIds.add("RES-EID-7");
		RepoTestUtil.linkResourcesToRole(tenant1, roleExternalId, resExternalIds);
		RoleResponse roleResponse = RepoTestUtil.getRole(tenant1, roleExternalId);
		
		Assertions.assertEquals(resExternalIds.size(), roleResponse.getResources().size());
		Assertions.assertTrue(roleResponse.getResources().get(0).getExternalId() != roleResponse.getResources().get(1).getExternalId());
		Assertions.assertTrue(roleResponse.getResources().get(0).getExternalId().equals(resExternalIds.get(0)) || roleResponse.getResources().get(0).getExternalId().equals(resExternalIds.get(1)) );

		
		//Unlink Resources from User
		resExternalIds = new ArrayList<String>();
		resExternalIds.add("RES-EID-3");
		resExternalIds.add("RES-EID-4");
		resExternalIds.add("RES-EID-5");
		RepoTestUtil.unlinkResourcesFromUser(tenant1, userExternalId, resExternalIds);
		userDto = RepoTestUtil.getUser(tenant1, userExternalId);		
		Assertions.assertEquals(0, userDto.getResources().size());
		
		//Unlink Roles from Users
		userExternalId = "USER-EID-3";
		roleExternalIds = new ArrayList<String>();
		roleExternalIds.add("ROLE-EID-3");
		roleExternalIds.add("ROLE-EID-4");
		roleExternalIds.add("ROLE-EID-7");
		RepoTestUtil.unlinkRolesFromUser(tenant1, userExternalId, roleExternalIds);
		userDto = RepoTestUtil.getUser(tenant1, userExternalId);
		Assertions.assertEquals(0, userDto.getRoles().size());
		
		//Unlink Resources from Roles
		roleExternalId = "ROLE-EID-4";
		resExternalIds = new ArrayList<String>();
		resExternalIds.add("RES-EID-6");
		resExternalIds.add("RES-EID-7");
		RepoTestUtil.unlinkResourcesFromRole(tenant1, roleExternalId, resExternalIds);
		roleResponse = RepoTestUtil.getRole(tenant1, roleExternalId);		
		Assertions.assertEquals(0, roleResponse.getResources().size());
		
		
		//Unlink Resources from Parent Resource
		parentResourceExternalId = "RES-EID-2";
		chieldResExternalIds = new ArrayList<String>();
		chieldResExternalIds.add("RES-EID-3");
		chieldResExternalIds.add("RES-EID-4");
		RepoTestUtil.unlinkResourcesFromParentResource(tenant1, parentResourceExternalId, chieldResExternalIds);
		resource = RepoTestUtil.getResource(tenant1, parentResourceExternalId);
		Assertions.assertEquals(0, resource.getSubResources().size());
		
		//Unlink Roles to Parent Role
		parentRoleExternalId = "ROLE-EID-2";
		chieldRoleExternalIds = new ArrayList<String>();
		chieldRoleExternalIds.add("ROLE-EID-3");
		chieldRoleExternalIds.add("ROLE-EID-4");
		RepoTestUtil.unlinkRolesFromParentRole(tenant1, parentRoleExternalId, chieldRoleExternalIds);
		role = RepoTestUtil.getRole(tenant1, parentRoleExternalId);		
		Assertions.assertEquals(0, role.getSubRoles().size());

		
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
