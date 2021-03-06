package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenantName;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String manager;
	private String organization;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public User() {
		
	}
	public User(String externalId, String userName, String firstName, String lastName, String email, String manager,
			String organization) {
		this.externalId = externalId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.manager = manager;
		this.organization = organization;

	}
}
