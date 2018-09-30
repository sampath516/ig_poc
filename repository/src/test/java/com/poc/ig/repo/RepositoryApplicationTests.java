package com.poc.ig.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryApplicationTests {

	@Test
	public void contextLoads() {
	}
	
//	@LocalServerPort
//	private int port;
//	
//	RestTemplate restClient = new RestTemplate();
//	private String tenantBasicUri = "http://localhost:8080" + "/ig/repo/v1/tenants";
//
//	@Test
//	public void testTenantCRUDOperations() {
//		CreateTenantRequest tenantIn = new CreateTenantRequest("abc", "ABC Tenant");
//		ResponseEntity<CreateTenantResponse> tenantOut = restClient.postForEntity(tenantBasicUri, tenantIn, CreateTenantResponse.class);
//		System.out.println("Success");
//	}
	
}
