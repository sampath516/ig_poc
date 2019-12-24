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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NodeEntity(label = "resource")
public class Resource implements Serializable {
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
}
