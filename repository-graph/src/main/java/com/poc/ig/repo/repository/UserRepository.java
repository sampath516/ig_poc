package com.poc.ig.repo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.repo.entity.User;

public interface UserRepository extends Neo4jRepository<User, Long> {

	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) match (u)-[r2:USER_BELONGS_TO_ORG]->(o:organization) "
			+ "WHERE ten.name = $tenantName AND usr.externalId = $userExternalId RETURN usr, r, ten, r2, o")
	public Optional<User> findByTenantNameAndUserExternalId(@Param("tenantName") String tenantName, @Param("userExternalId") String userExternalId);
	
	public Optional<User> findByExternalId(String userExternalId);
	

	@Query("MATCH (usr: user)-[r:USER_BELONGS_TO_TEN]->(ten: tenant) WHERE ten.name = $tenantName RETURN usr, r, ten")
	public List<User> findByTenantName(@Param("tenantName") String tenantName);
	
	@Query("match (u:user)-[r1:USER_ASSIGNED_RES]->(res:resource) "
		+ "match(manager:user)-[r2:MANAGER_OF]-(u) "
		+ "match(owner:user)-[r3:OWNER_OF_RES]->(res) "
		+ "match(u)-[r4:USER_BELONGS_TO_TEN]->(t:tenant) "
		+ "match(res)-[r5:RES_BELONGS_TO_TEN]->(t)  "
		+ "match(u)-[r6:USER_BELONGS_TO_ORG]->(org:organization) "
		+ "match(res)-[r7:RES_BELONGS_APP]->(app:application) "
		+ "where t.name = $tenantName and org.name = $orgName "
		+ "return u, r1, res, r2, manager, r3, owner,r4,t,r5,r6,org,r7, app")
	public List<User> findAllUserResourceEntitlementsByTenantNameAndOrganizationName(@Param("tenantName") String tenantName, @Param("orgName") String orgName);

	@Query("match (u:user)-[r1:USER_ASSIGNED_RES]->(res:resource) " + 
			"match(u)-[r2:USER_BELONGS_TO_TEN]->(t:tenant) " + 
			"match(res)-[r3:RES_BELONGS_TO_TEN]->(t) " + 
			"match(u)-[r4:USER_BELONGS_TO_ORG]->(org:organization) " + 
			"where t.name = $tenantName and org.name = $orgName \r\n" + 
			"return count(r1)")
	public int findUserResourceEntitlementsCountByTenantNameAndOrganizationName(@Param("tenantName") String tenantName, @Param("orgName") String orgName);

}
