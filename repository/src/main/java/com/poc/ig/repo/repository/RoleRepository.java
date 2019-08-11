package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

	@Query(" select r from Role r where r.organization.id = :orgId and r.id = :roleId")
	public Role findByOrgIdAndRoleId(@Param("orgId") long orgId, @Param("roleId") long roleId);
	
	@Query(" select r from Role r where r.tenant.name = :tenantName and r.externalId = :roleExternalId")
	public Optional<Role> findByTenantNameAndRoleExternalId(@Param("tenantName") String tenantName, @Param("roleExternalId") String roleExternalId);
	
	@Query(" select r from Role r where r.tenant.name = :tenantName ")
	public List<Role> findByTenantName(@Param("tenantName") String tenantName);

}
