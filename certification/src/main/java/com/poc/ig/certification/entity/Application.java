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
@NodeEntity(label = "application")
public class Application  extends AbstractEntity  {
	
	private String type;
		
	@Relationship(type = "APP_BELONGS_TO_TEN", direction = Relationship.OUTGOING)
	private Tenant tenant;
	
	@Relationship(type = "APP_BELONGS_TO_ORG", direction = Relationship.OUTGOING)
	private Organization organization;

	@Relationship(type = "OWNER_OF_APP", direction = Relationship.INCOMING)
	private User owner;

	@Relationship(type = "RES_BELONGS_APP", direction = Relationship.INCOMING)
	private Set<Resource> resources = new HashSet<Resource>();

}
