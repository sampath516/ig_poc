package com.poc.ig.certification.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.poc.ig.certification.entity.Entitlement;

public interface EntitlementRepository extends Neo4jRepository<Entitlement, String> {

}
