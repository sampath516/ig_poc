package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Role;

public interface RoleRepository extends Neo4jRepository<Role, Long> {
	
	@Query("MATCH (role: role) -[r:ROLE_BELONGS_TO_TEN]-> (ten: tenant) WHERE ten.name=$tenantName AND role.externalId = $roleExternalId RETURN role,r,ten")
	public Optional<Role> findByTenantNameAndRoleExternalId(@Param("tenantName") String tenantName, @Param("roleExternalId") String roleExternalId);
	
	public Optional<Role> findByExternalId(String roleExternalId);
	
	@Query("MATCH (role: role)-[r:ROLE_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name = $tenantName RETURN role, r, ten")
	public List<Role> findByTenantName(@Param("tenantName") String tenantName);

}
