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
@NodeEntity(label = "resource")
public class Resource extends AbstractEntity {

	@Relationship(type = "RES_BELONGS_TO_TEN", direction = Relationship.OUTGOING)
	private Tenant tenant;

	@Relationship(type = "RES_BELONGS_APP", direction = Relationship.OUTGOING)
	private Application application;

	@Relationship(type = "ROLE_ASSIGNED_RES", direction = Relationship.INCOMING)
	private Set<Role> roles = new HashSet<Role>();

	@Relationship(type = "USER_ASSIGNED_RES", direction = Relationship.INCOMING)
	private Set<User> users = new HashSet<User>();

	@Relationship(type = "OWNER_OF_RES", direction = Relationship.INCOMING)
	private User owner;

	@Relationship(type = "CHIELD_OF_RES", direction = Relationship.OUTGOING)
	private Resource parent;

	@Relationship(type = "CHIELD_OF_RES", direction = Relationship.INCOMING)
	private Set<Resource> subResources = new HashSet<Resource>();

	@Relationship(type = "RES_BELONGS_TO_CERT", direction = Relationship.OUTGOING)
	private Certification certification;
}
