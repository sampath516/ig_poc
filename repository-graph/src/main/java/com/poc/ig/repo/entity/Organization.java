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
@NodeEntity(label = "organization")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
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
	
	@EqualsAndHashCode.Include
	@Relationship(type = "HAS", direction = Relationship.INCOMING)
	private Tenant tenant;
	
	@Relationship(type = "APP_BELONGS_TO_ORG", direction = Relationship.INCOMING)
	private Set<Application> applications = new HashSet<Application>();
	
	@Relationship(type = "USER_BELONGS_TO_ORG", direction = Relationship.INCOMING)
	private Set<User> users = new HashSet<User>();
	
	@Relationship(type = "ROLE_BELONGS_TO_ORG", direction = Relationship.INCOMING)
	private Set<Role> roles = new HashSet<Role>(100);

}
