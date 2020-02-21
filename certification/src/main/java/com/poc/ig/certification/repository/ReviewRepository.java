package com.poc.ig.certification.repository;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import com.poc.ig.certification.entity.Review;
import com.poc.ig.certification.entity.Review.ReviewState;

public interface ReviewRepository extends Neo4jRepository<Review, String> {
	
	@Query("match(ten:tenant)<-[r1:CERT_BELONGS_TO_TEN]-(cert:certification) "
			+ "match(cert)-[r2:CERT_HAS_ENTITLEMENT]->(ent:`user-prev-res-entitlement`) "
			+ "match(ent)<-[r3:REVIEW_BELONGS_TO_ENTMT]-(rev:review) "
			+ "match(owner:user)-[r4:OWNER_OF_REVIEW]->(rev) "
			+ "match(ent)-[r5:PRIMARY_ENTITY]->(pe:user) "
			+ "match(ent)-[r6:SECONDARY_ENTITY]->(se:resource) "
			+ "where  ten.name = $tenantName and cert.name= $certificationName and owner.externalId= $reviewer "
			+ "and  (cert.state=\"CREATE\" or cert.state=\"IN_PROGRESS\") "
			+ "and ent.state <> \"CLOSED\" and rev.state= $state "
			+ "return ent,rev, pe, se, owner, r1,r2,r3,r4,r5,r6")
		public Set<Review> findAllUserResourceReviews(@Param("tenantName")String tenantName, @Param("certificationName") String certificationName, @Param("reviewer") String reviewer, @Param("state") ReviewState state);


}
