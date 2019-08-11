package com.poc.ig.repo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

	@Query("select t from Tenant t where t.name = :name")
	public Optional<Tenant> findByName(@Param("name") String name);

}
