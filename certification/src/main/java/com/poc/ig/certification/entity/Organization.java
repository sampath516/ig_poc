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
@NodeEntity(label = "organization")
public class Organization extends AbstractEntity {

	@Relationship(type = "HAS", direction = Relationship.INCOMING)
	private Tenant tenant;

	@Relationship(type = "APP_BELONGS_TO_ORG", direction = Relationship.INCOMING)
	private Set<Application> applications = new HashSet<Application>();

	@Relationship(type = "USER_BELONGS_TO_ORG", direction = Relationship.INCOMING)
	private Set<User> users = new HashSet<User>();

	@Relationship(type = "ROLE_BELONGS_TO_ORG", direction = Relationship.INCOMING)
	private Set<Role> roles = new HashSet<Role>(100);

}
