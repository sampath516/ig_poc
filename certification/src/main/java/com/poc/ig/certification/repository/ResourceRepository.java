package com.poc.ig.certification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.Resource;



public interface ResourceRepository extends Neo4jRepository<Resource, String> {

	@Query("MATCH (res: resource) -[r:RES_BELONGS_TO_TEN]-> (ten: tenant) WHERE ten.name=$tenantName AND res.externalId = $resExternalId RETURN res,r,ten")
	public Optional<Resource> findByTenantNameAndResourceExternalId(@Param("tenantName") String tenantName, @Param("resExternalId") String resExternalId);
	
	public Optional<Resource> findByExternalId(String resourceExternalId);
	
	@Query("MATCH (res: resource)-[r:RES_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name = $tenantName RETURN res, r, ten")
	public List<Resource> findByTenantName(@Param("tenantName") String tenantName);
	
	@Query("match (res:resource)<-[r1:USER_ASSIGNED_RES]-(u:user) "
			+ "match  (res)-[r2:RES_BELONGS_TO_TEN]->(t:tenant) "
			+ "match (res) -[r3]-(other) "
			+ "where t.name= $tenantName and u.externalId= $userExternalId  "
			+ "return res, r1, r2, t, r3, other")
	public List<Resource> findAllByTenantNameAndAssignedUser(@Param("tenantName") String tenantName, @Param("userExternalId") String userExternalId);
	
	@Query("MATCH (res: resource)-[r:RES_BELONGS_TO_TEN]->(ten: tenant) match (u)-[r2:RES_BELONGS_TO_CERT]->(cert:certification) "
			+ "WHERE ten.name = $tenantName AND res.externalId = $resExternalId AND cert.name=$certName RETURN res, r, ten, r2, cert")
	public Optional<Resource> findByTenantNameAndCertNameAndUserExternalId(@Param("tenantName") String tenantName, @Param("resExternalId") String resExternalId, @Param("certName") String certName);

}
