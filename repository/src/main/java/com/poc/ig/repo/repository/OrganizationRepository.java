package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	
	public List<Organization> findByNameContains(String orgName);
	
	@Query("select org from Organization org where org.tenant.name = :tenantName")
	public List<Organization> findByTenantName(@Param("tenantName") String tenantName);
	
	@Query("select org from Organization org where org.tenant.id = :tenantId and org.id = :orgId")
	public Organization findByTenantIdAndOrgId(@Param("tenantId") long tenantId, @Param("orgId") long orgId);
	
	@Query("select org from Organization org where org.tenant.name = :tenantName and org.name = :orgName")
	public Organization findByTenantNameAndOrgName(@Param("tenantName") String tenantName, @Param("orgName") String orgName);
	
	@Query("select org from Organization org where org.tenant.name = :tenantName and org.externalId = :orgExternalId")
	public Optional<Organization> findByTenantNameAndOrgExternalId(@Param("tenantName") String tenantName, @Param("orgExternalId") String orgExternalId);

}
 