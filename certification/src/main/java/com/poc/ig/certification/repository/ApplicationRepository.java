package com.poc.ig.certification.repository;

import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.Application;



public interface ApplicationRepository extends Neo4jRepository<Application, String> {

	@Query("MATCH (app: application) -[r:APP_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name=$tenantName AND app.externalId = $appExternalId RETURN app, r, ten")
	public Optional<Application> findByTenantNameAndAppExternalId(@Param("tenantName") String tenantName, @Param("appExternalId") String appExternalId);

}
