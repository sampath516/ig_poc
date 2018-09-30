package com.poc.ig.repo.entity;

import java.io.Serializable;
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
	private String id;
	@Column(nullable=false, unique=true)
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID", nullable = false)
	private Organization organization;
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="USER_ROLE", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
	private List<Role> roles = new ArrayList<>();
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="USER_RESOURCE", joinColumns=@JoinColumn(name="USER_ID"), inverseJoinColumns=@JoinColumn(name="ROSOURCE_ID"))
	private List<Resource> resources = new ArrayList<>();

}
