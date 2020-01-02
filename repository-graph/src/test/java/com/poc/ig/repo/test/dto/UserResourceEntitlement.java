package com.poc.ig.repo.test.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Setter
@Getter
@NoArgsConstructor
public class UserResourceEntitlement  implements  Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tenantName;	
	private String organization;
	private UserDto primaryEntity;
	private ResourceDto secondaryEntity;	
	
	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class UserDto{
		private String externalId;
		private String name;			
		private String firstName;
		private String lastName;
		private String email;
		private String manager;

		public UserDto(String externalId, String name, String firstName, String lastName, String email, String manager) {
			this.externalId = externalId;
			this.name = name;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.manager = manager;
		}
		
	}
	
	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class ResourceDto{
		private String externalId;
		private String name;
		private String description;
		private String application;
		private String owner;
		public ResourceDto(String externalId, String name, String description, String application, String owner) {
			super();
			this.externalId = externalId;
			this.name = name;
			this.description = description;
			this.application = application;
			this.owner = owner;
		}
	}
}
