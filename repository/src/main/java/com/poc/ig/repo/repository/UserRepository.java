package com.poc.ig.repo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u where u.organization.id = :orgId and u.id = :userId")
	public User findByOrgIdAndUserId(@Param("orgId") long orgId, @Param("userId") long userId);

}
