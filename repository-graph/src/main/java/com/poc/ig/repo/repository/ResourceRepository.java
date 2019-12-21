package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Resource;

public interface ResourceRepository extends Neo4jRepository<Resource, Long> {

	@Query("MATCH (res: resource) -[r:RES_BELONGS_TO_TEN]-> (ten: tenant) WHERE ten.name=$tenantName AND res.externalId = $resExternalId RETURN res,r,ten")
	public Optional<Resource> findByTenantNameAndResourceExternalId(@Param("tenantName") String tenantName, @Param("resExternalId") String resExternalId);
	
	@Query("MATCH (res: resource)-[r:RES_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name = $tenantName RETURN res, r, ten")
	public List<Resource> findByTenantName(@Param("tenantName") String tenantName);

}
