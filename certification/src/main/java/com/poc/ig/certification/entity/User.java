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
@NodeEntity(label = "user")
public class User extends AbstractEntity {

	private String firstName;
	private String lastName;
	private String email;

	@Relationship(type = "MANAGER_OF", direction = Relationship.INCOMING)
	private User manager;

	@Relationship(type = "USER_BELONGS_TO_TEN", direction = Relationship.OUTGOING)
	private Tenant tenant;

	@Relationship(type = "USER_BELONGS_TO_ORG", direction = Relationship.OUTGOING)
	private Organization organization;

	@Relationship(type = "USER_ASSIGNED_ROLE", direction = Relationship.OUTGOING)
	private Set<Role> roles = new HashSet<>();

	@Relationship(type = "USER_ASSIGNED_RES", direction = Relationship.OUTGOING)
	private Set<Resource> resources = new HashSet<>();

	@Relationship(type = "OWNER_OF_RES", direction = Relationship.OUTGOING)
	private Set<Resource> ownedResources = new HashSet<>();

	@Relationship(type = "OWNER_OF_ROLE", direction = Relationship.OUTGOING)
	private Set<Role> ownedRoles = new HashSet<>();

	@Relationship(type = "OWNER_OF_CERT", direction = Relationship.OUTGOING)
	private Set<Certification> ownedCertifications = new HashSet<>();

	@Relationship(type = "USER_BELONGS_TO_CERT", direction = Relationship.OUTGOING)
	private Certification certification;

	@Relationship(type = "REVIEWER_OF_CERT", direction = Relationship.OUTGOING)
	private Certification reviewCertification;

}
