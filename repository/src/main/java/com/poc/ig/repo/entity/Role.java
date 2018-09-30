package com.poc.ig.repo.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String name;
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID", nullable = false)
	private Organization organization;
	@ManyToMany(mappedBy = "roles")
	private List<User> users;
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="ROLE_RESOURCE", joinColumns=@JoinColumn(name="ROLE_ID"), inverseJoinColumns=@JoinColumn(name="RESOURCE_ID"))
	private List<Resource> resources;

}
