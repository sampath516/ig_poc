package com.poc.ig.imp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.imp.clients.repo.RepositoryRestClient;
import com.poc.ig.imp.clients.repo.dto.Tenant;

@RestController
@RequestMapping(value = "ig/import/v1/tenants/{tenantName}/repository", produces = "application/json", consumes = "application/json")
public class RepositoryService {

	@Autowired
	RepositoryRestClient repositoryClient;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping()
	public boolean createRepository(@PathVariable String tenantName) {
		boolean result = true;
		Tenant tenant = repositoryClient.getTenant(tenantName);
		System.out.println(tenant.getName());
		return result;
	}

}
