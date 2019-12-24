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
@NodeEntity(label = "role")
public class Role implements Serializable {
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
    private Role  parent;
    
    @Relationship(type = "CHIELD_OF_ROLE", direction = Relationship.INCOMING)	
    private Set<Role> subRoles = new HashSet<Role>();

}
