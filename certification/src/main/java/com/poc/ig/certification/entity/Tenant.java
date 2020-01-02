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
@NodeEntity(label = "tenant")
public class Tenant extends AbstractEntity {

	@Relationship(type = "HAS", direction = Relationship.OUTGOING)
	private Set<Organization> organizations = new HashSet<>();

	@Relationship(type = "CERT_BELONGS_TO_TEN", direction = Relationship.INCOMING)
	private Set<Certification> certifications = new HashSet<Certification>();
}
