package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Role;
import com.poc.ig.repo.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserResponse implements Serializable {
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
	private List<RoleDto> roles = new ArrayList<UserResponse.RoleDto>();
	private List<ResourceDto> resources = new ArrayList<UserResponse.ResourceDto>();

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public UserResponse(User user) {
		this.id = user.getId();
		this.externalId = user.getExternalId();
		this.tenantName = user.getTenant().getName();
		this.userName = user.getUserName();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		if (user.getManager() != null) {
			this.manager = user.getManager().getUserName();
		}
		if(user.getOrganization() != null) {
			this.organization = user.getOrganization().getName();
		}		

		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				this.roles.add(new RoleDto(role.getId(), role.getExternalId(), role.getName()));
			}
		}
		
		if(user.getResources() != null) {
			for(Resource res : user.getResources()) {
				this.resources.add(new ResourceDto(res.getId(), res.getExternalId(), res.getName()));
			}
		}

		this.createdAt = user.getCreatedAt();
		this.updatedAt = user.getUpdatedAt();
	}

	@Data
	@Getter
	@Setter
	public static class RoleDto {
		private long id;
		private String externalId;
		private String name;

		public RoleDto() {

		}

		public RoleDto(long id, String externalId, String name) {
			this.id = id;
			this.externalId = externalId;
			this.name = name;
		}
	}

	@Data
	@Getter
	@Setter
	public static class ResourceDto {
		private long id;
		private String externalId;
		private String name;

		public ResourceDto() {

		}

		public ResourceDto(long id, String externalId, String name) {
			this.id = id;
			this.externalId = externalId;
			this.name = name;
		}

	}

}
