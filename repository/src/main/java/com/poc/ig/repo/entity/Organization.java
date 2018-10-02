package com.poc.ig.repo.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;

	@ManyToOne
	@JoinColumn(name = "tenant_id", nullable=false)
	private Tenant tenant;
	@OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST)
	private List<User> users;
	@OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST)
	private List<Role> roles;
	@OneToMany(mappedBy = "organization", cascade = CascadeType.PERSIST)
	private List<Resource> resources;

}
