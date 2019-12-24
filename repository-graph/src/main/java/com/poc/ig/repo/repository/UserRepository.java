package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.User;

public interface UserRepository extends Neo4jRepository<User, Long> {

	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) match (u)-[r2:USER_BELONGS_TO_ORG]->(o:organization) "
			+ "WHERE ten.name = $tenantName AND usr.externalId = $userExternalId RETURN usr, r, ten, r2, o")
	public Optional<User> findByTenantNameAndUserExternalId(@Param("tenantName") String tenantName, @Param("userExternalId") String userExternalId);
	
	public Optional<User> findByExternalId(String userExternalId);
	

	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name = $tenantName RETURN usr, r, ten")
	public List<User> findByTenantName(@Param("tenantName") String tenantName);

}
