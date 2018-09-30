package com.poc.ig.repo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
	
	public List<Organization> findByNameContains(String orgName);
	
	@Query("select org from Organization org where org.tenant.id = :tenantId")
	public List<Organization> findByTenantId(@Param("tenantId") String tenantId);
	
	@Query("select org from Organization org where org.tenant.id = :tenantId and org.id = :orgId")
	public Organization findByTenantIdAndOrgId(@Param("tenantId") String tenantId, @Param("orgId") String orgId);

}
 