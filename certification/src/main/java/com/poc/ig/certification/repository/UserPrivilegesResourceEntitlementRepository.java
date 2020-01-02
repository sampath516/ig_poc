package com.poc.ig.certification.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;

public interface UserPrivilegesResourceEntitlementRepository
		extends Neo4jRepository<UserPrivilegesResourceEntitlement, String> {
	
	
}
