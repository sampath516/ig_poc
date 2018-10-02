package com.poc.ig.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.ig.repo.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
