package com.poc.ig.certification.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.poc.ig.certification.entity.CertificationReviewLogic;

public interface CertificationReviewLogicRepository extends Neo4jRepository<CertificationReviewLogic, String> {

}
