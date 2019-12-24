package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
public class RoleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenant;
	private String name;
	private String description;
	private User owner;
	private Organization organization;
	private List<ResourceDto> resources = new ArrayList<RoleResponse.ResourceDto>();
	private RoleDto parent;
	private List<RoleDto> subRoles = new ArrayList<RoleResponse.RoleDto>();

	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class User {
		private String externalId;
		private String name;

		public User(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}

	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Organization {
		private String externalId;
		private String name;

		public Organization(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}

	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ResourceDto {
		private String externalId;
		private String name;

		public ResourceDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}

	}
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class RoleDto {
		private String externalId;
		private String name;

		public RoleDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}

	}
}
