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
@NodeEntity(label = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	private Long id;
	@EqualsAndHashCode.Include
	private String externalId;
	@EqualsAndHashCode.Include
	private String userName;
	private String firstName;
	private String lastName;

	private String email;

	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	

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

}
