package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.certification.entity.Resource;
import com.poc.ig.certification.entity.Role;
import com.poc.ig.certification.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String tenantName;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String manager;
	private String organization;
	private List<RoleDto> roles = new ArrayList<UserResponse.RoleDto>();
	private List<ResourceDto> resources = new ArrayList<UserResponse.ResourceDto>();

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public UserResponse(User user) {
		this.externalId = user.getExternalId();
		this.tenantName = user.getTenant().getName();
		this.name = user.getName();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		if (user.getManager() != null) {
			this.manager = user.getManager().getExternalId();
		}
		if (user.getOrganization() != null) {
			this.organization = user.getOrganization().getExternalId();
		}

		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				this.roles.add(new RoleDto(role.getExternalId(), role.getName()));
			}
		}

		if (user.getResources() != null) {
			for (Resource res : user.getResources()) {
				this.resources.add(new ResourceDto(res.getExternalId(), res.getName()));
			}
		}

		this.createdAt = user.getCreatedAt();
		this.updatedAt = user.getUpdatedAt();
	}

	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class RoleDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;

		public RoleDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}

	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ResourceDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;

		public ResourceDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}

	}

}
