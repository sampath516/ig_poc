package com.poc.ig.certification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.Organization;


public interface OrganizationRepository extends Neo4jRepository<Organization, String> {
	
	@Query("MATCH (org: organization) <-[h:HAS]-(ten: tenant) WHERE ten.name = $tenantName RETURN org, h, ten")
	public List<Organization> findByTenantName(@Param("tenantName") String tenantName);
	
	@Query("MATCH (org: organization) <-[h:HAS]-(ten: tenant) WHERE ten.name=$tenantName AND org.externalId = $orgExternalId RETURN org,h,ten")
	public Optional<Organization> findByTenantNameAndOrgExternalId(@Param("tenantName") String tenantName, @Param("orgExternalId") String orgExternalId);

}
 