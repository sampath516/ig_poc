package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto implements Serializable{

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
	private List<RoleDto> roles;
	private List<ResourceDto> resources;
	
	
	
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	public UserDto() {
		
	}
	public UserDto(String externalId, String userName, String firstName, String lastName, String email, String manager,
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
	
	@Data
	@Getter
	@Setter
	public static class RoleDto{
		private long id;
		private String externalId;
		private String name;
	}
	
	@Data
	@Getter
	@Setter
	public static class ResourceDto{
		private long id;
		private String externalId;
		private String name;
	}
}
