package com.poc.ig.certification.repository;

import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.Certification;

public interface CertificationRepository extends Neo4jRepository<Certification, String> {
	public Optional<Certification> findByName(@Param("name") String name);
	
	@Query("MATCH (cert: certification)-[r:CERT_BELONGS_TO_TEN]->(ten: tenant)"
			+ "WHERE ten.name = $tenantName AND cert.name = $name RETURN cert, r, ten")
	public Optional<Certification> findByTenantNameAndName(@Param("tenantName") String tenantName, @Param("name") String name);

}
