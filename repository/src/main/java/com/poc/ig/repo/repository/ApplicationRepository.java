package com.poc.ig.repo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	@Query(" select a from Application a where a.tenant.name = :tenantName and a.externalId = :appExternalId ")
	public Optional<Application> findByTenantNameAndAppExternalId(@Param("tenantName") String tenantName, @Param("appExternalId") String appExternalId);

}
