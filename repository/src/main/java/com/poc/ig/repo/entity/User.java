package com.poc.ig.repo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "external_id",nullable=false, unique=true)
	private String externalId;
	@Column(name="username", nullable=false)
	private String userName;
	@Column(name="firstname")
	private String firstName;
	@Column(name="lastname")
	private String lastName;
	private String email;
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;	
	@PrePersist
	private void onCreate() {
		createdAt = updatedAt = LocalDateTime.now();
	}
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;	
	@PreUpdate
	private void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	@ManyToOne
	@JoinColumn(name = "tenant_id", nullable=false)
	private Tenant tenant;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager", nullable = false)
	private User manager;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization organization;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
	private List<Role> roles = new ArrayList<>();
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="user_resource", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="resource_id"))
	private List<Resource> resources = new ArrayList<>();
	
	

}
