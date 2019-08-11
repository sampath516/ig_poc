package com.poc.ig.repo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Application implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "external_id",nullable=false, unique=true)
	private String externalId;
	private String name;
	private String description;
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

	@ManyToOne
	@JoinColumn(name = "org_id", nullable=false)
	private Organization organization;
	
	@ManyToOne
	@JoinColumn(name = "owner", nullable=false)
	private User owner;
	
	@OneToMany(mappedBy = "application", cascade = CascadeType.PERSIST)
	private List<Resource> resources;
	
}
