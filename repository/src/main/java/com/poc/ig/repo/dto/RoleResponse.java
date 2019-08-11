package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
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

		
	public RoleResponse(Role role) {
		this.id = role.getId();
		this.externalId = role.getExternalId();
		this.tenant = role.getTenant().getName();
		this.name = role.getName();
		this.description = role.getDescription();
		this.owner = new User(role.getOwner().getExternalId(), role.getOwner().getUserName());
		this.organization = new Organization(role.getOrganization().getExternalId(), role.getOrganization().getName());
	}	
	
	@Data
	@Getter
	@Setter
	public static class User {
		private String externalId;
		private String name;
		
		public User(String externalId, String name ) {
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
		public Organization(String externalId, String name ) {
			this.externalId = externalId;
			this.name = name;
		}
	}

}
