package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String externalId;
	private String tenantName;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String manager;
	private String certificationName;
	private String organization;
	private List<RoleDto> roles;
	private List<ResourceDto> resources;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public UserDto() {

	}

	public UserDto(String externalId, String userName, String firstName, String lastName, String email, String manager,
			String organization, String certificationName) {
		this.externalId = externalId;
		this.name = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.manager = manager;
		this.organization = organization;
		this.certificationName = certificationName;

	}

	@Data
	@Getter
	@Setter
	public static class RoleDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;
	}

	@Data
	@Getter
	@Setter
	public static class ResourceDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;
	}
}
