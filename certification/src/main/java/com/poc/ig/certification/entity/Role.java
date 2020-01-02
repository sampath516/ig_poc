package com.poc.ig.certification.entity;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@NodeEntity(label = "role")
public class Role extends AbstractEntity {

	@Relationship(type = "OWNER_OF_ROLE", direction = Relationship.INCOMING)
	private User owner;

	@Relationship(type = "ROLE_BELONGS_TO_TEN", direction = Relationship.OUTGOING)
	private Tenant tenant;

	@Relationship(type = "ROLE_BELONGS_TO_ORG", direction = Relationship.OUTGOING)
	private Organization organization;

	@Relationship(type = "USER_ASSIGNED_ROLE", direction = Relationship.INCOMING)
	private Set<User> users = new HashSet<User>();

	@Relationship(type = "ROLE_ASSIGNED_RES", direction = Relationship.OUTGOING)
	private Set<Resource> resources = new HashSet<Resource>();

	@Relationship(type = "CHIELD_OF_ROLE", direction = Relationship.OUTGOING)
	private Role parent;

	@Relationship(type = "CHIELD_OF_ROLE", direction = Relationship.INCOMING)
	private Set<Role> subRoles = new HashSet<Role>();

}
