package com.poc.ig.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

	@Query("select r from Resource r where r.organization.id = :orgId and r.id = :resourceId")
	public Resource findByOrgIdAndResourceId(@Param("orgId") long orgId, @Param("resourceId") long resourceId);

}
