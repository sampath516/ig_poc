package com.poc.ig.certification.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserPrivilegesResEntitlementRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private UserDto primaryEntity;
	private ResourceDto secondaryEntity;
	
	@Data
	@Getter
	@Setter
	public static class UserDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String userName;
		private String firstName;
		private String lastName;
		private String email;
		private String manager;
		private String organization;

	}
	
	@Data
	@Getter
	@Setter
	public static class ResourceDto implements Serializable{
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;
		private String description;
		private String application;
		private String owner;
	}
	
}
