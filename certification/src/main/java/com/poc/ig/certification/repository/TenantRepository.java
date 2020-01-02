package com.poc.ig.certification.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.Tenant;



public interface TenantRepository extends Neo4jRepository<Tenant, String> {

	public Optional<Tenant> findByName(@Param("name") String name);

}
