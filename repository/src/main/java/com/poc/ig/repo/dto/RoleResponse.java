package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private String name;
	private String description;
	
	private Organization organization;
	private List<User> users = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();
	
	
	public RoleResponse(Role role) {
		this.id = role.getId();
		this.name = role.getName();
		this.description = role.getDescription();
		
		this.organization = new Organization();
		this.organization.setId(role.getOrganization().getId());
		this.organization.setName(role.getOrganization().getName());
		this.organization.setDescription(role.getOrganization().getDescription());
		
		for(com.poc.ig.repo.entity.User u: role.getUsers()) {
			User userDto = new User();
			userDto.setId(u.getId());
			userDto.setUserName(u.getUserName());
			userDto.setFirstName(u.getFirstName());
			userDto.setLastName(u.getLastName());
			userDto.setEmail(u.getEmail());
			users.add(userDto);
		}
		
		for(com.poc.ig.repo.entity.Resource res : role.getResources()) {
			Resource resDto = new Resource();
			resDto.setId(res.getId());
			resDto.setName(res.getName());
			resDto.setDescription(res.getDescription());
			resources.add(resDto);
		}
	}	

	@Data
	@Getter
	@Setter
	public static class Organization {
		private long id;
		private String name;
		private String description;
	}

	@Data
	@Getter
	@Setter
	public static class User {
		private long id;
		private String userName;
		private String firstName;
		private String lastName;
		private String email;
	}

	@Data
	@Getter
	@Setter
	public static class Resource {
		private long id;
		private String name;
		private String description;
	}

}
