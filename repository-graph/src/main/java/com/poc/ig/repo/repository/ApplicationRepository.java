package com.poc.ig.repo.repository;

import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Application;

public interface ApplicationRepository extends Neo4jRepository<Application, Long> {


	@Query("MATCH (app: application) -[r1:APP_BELONGS_TO_TEN]->(ten: tenant) MATCH(app)<-[r2:OWNER_OF_APP]-(owner:user)  WHERE ten.name = $tenantName AND app.externalId = $appExternalId RETURN app, r1, ten, r2, owner")
	public Optional<Application> findByTenantNameAndAppExternalId(@Param("tenantName") String tenantName, @Param("appExternalId") String appExternalId);

}
