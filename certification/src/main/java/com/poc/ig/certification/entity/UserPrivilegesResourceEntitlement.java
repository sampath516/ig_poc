package com.poc.ig.certification.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@NodeEntity(label = "user-prev-res-entitlement")
public class UserPrivilegesResourceEntitlement extends Entitlement {

	@Relationship(type = "PRIMARY_ENTITY", direction = Relationship.OUTGOING)
	private User primaryEntity;

	@Relationship(type = "SECONDARY_ENTITY", direction = Relationship.OUTGOING)
	private Resource secondaryEntity;
}
