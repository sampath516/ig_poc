package com.poc.ig.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

	@Query("select r from Role r where r.organization.id = :orgId and r.id = :roleId")
	public Role findByOrgIdAndRoleId(@Param("orgId") String orgId, @Param("roleId") String roleId);

}
