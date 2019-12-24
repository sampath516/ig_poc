package com.poc.ig.repo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@NodeEntity(label = "application")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Application implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	private Long id;
	
	@EqualsAndHashCode.Include
	private String externalId;
	
	@EqualsAndHashCode.Include
	private String name;
	
	private String description;
	
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	
	@Relationship(type = "APP_BELONGS_TO_TEN", direction = Relationship.OUTGOING)
	private Tenant tenant;
	
	@Relationship(type = "APP_BELONGS_TO_ORG", direction = Relationship.OUTGOING)
	private Organization organization;

	@Relationship(type = "OWNER_OF_APP", direction = Relationship.INCOMING)
	private User owner;

	@Relationship(type = "RES_BELONGS_APP", direction = Relationship.INCOMING)
	private Set<Resource> resources = new HashSet<Resource>();

}
