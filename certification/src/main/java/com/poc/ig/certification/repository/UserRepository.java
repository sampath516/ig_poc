package com.poc.ig.certification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.User;



public interface UserRepository extends Neo4jRepository<User, String> {

	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) match (u)-[r2:USER_BELONGS_TO_ORG]->(o:organization) "
			+ "WHERE ten.name = $tenantName AND usr.externalId = $userExternalId RETURN usr, r, ten, r2, o")
	public Optional<User> findByTenantNameAndUserExternalId(@Param("tenantName") String tenantName, @Param("userExternalId") String userExternalId);
	
	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) "
			+ "match (u)-[r2:USER_BELONGS_TO_CERT]->(cert:certification) "
			+ "match (u)<-[r3:MANAGER_OF]-(manager:user)"
			+ "WHERE ten.name = $tenantName AND usr.externalId = $userExternalId AND cert.name=$certName RETURN usr, r, ten, r2, cert, r3, manager")
	public Optional<User> findByTenantNameAndCertNameAndUserExternalId(@Param("tenantName") String tenantName, @Param("userExternalId") String userExternalId, @Param("certName") String certName);
	
	public Optional<User> findByExternalId(String userExternalId);
	

	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name = $tenantName RETURN usr, r, ten")
	public List<User> findByTenantName(@Param("tenantName") String tenantName);

}
