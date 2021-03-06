package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Role;

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

	public RoleResponse(Role role) {
		this.id = role.getId();
		this.externalId = role.getExternalId();
		this.tenant = role.getTenant().getName();
		this.name = role.getName();
		this.description = role.getDescription();
		if (role.getOwner() != null) {
			this.owner = new User(role.getOwner().getExternalId(), role.getOwner().getUserName());
		}
		if (role.getOrganization() != null) {
			this.organization = new Organization(role.getOrganization().getExternalId(),
					role.getOrganization().getName());
		}
		if (role.getResources() != null) {
			for (Resource res : role.getResources()) {
				resources.add(new ResourceDto(res.getExternalId(), res.getName()));
			}
		}
		if(role.getParent() != null) {
			this.parent = new RoleDto(role.getParent().getExternalId(), role.getParent().getName());
		}
		if(role.getSubRoles() != null) {
			for(Role r : role.getSubRoles()) {
				this.subRoles.add(new RoleDto(r.getExternalId(), r.getName()));
			}
		}
	}

	@Data
	@Getter
	@Setter
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
