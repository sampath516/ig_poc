package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

	@Query(" select r from Resource r where r.tenant.name = :tenantName and r.externalId = :resExternalId ")
	public Optional<Resource> findByTenantNameAndResourceExternalId(@Param("tenantName") String tenantName, @Param("resExternalId") String resExternalId);
	
	@Query(" select r from Resource r where r.tenant.name = :tenantName ")
	public List<Resource> findByTenantName(@Param("tenantName") String tenantName);

}
