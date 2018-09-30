package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.repo.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;
	
	private Organization organization;
	private List<User> users = new ArrayList<>();
	private List<Role> roles = new ArrayList<>();
	
	
	public ResourceResponse(Resource resource) {
		this.id = resource.getId();
		this.name = resource.getName();
		this.description = resource.getDescription();
		
		this.organization = new Organization();
		this.organization.setId(resource.getOrganization().getId());
		this.organization.setName(resource.getOrganization().getName());
		this.organization.setDescription(resource.getOrganization().getDescription());
		
		for(com.poc.ig.repo.entity.User u: resource.getUsers()) {
			User userDto = new User();
			userDto.setId(u.getId());
			userDto.setUserName(u.getUserName());
			userDto.setFirstName(u.getFirstName());
			userDto.setLastName(u.getLastName());
			userDto.setEmail(u.getEmail());
			users.add(userDto);
		}
		
		for(com.poc.ig.repo.entity.Role role : resource.getRoles()) {
			Role roleDto = new Role();
			roleDto.setId(role.getId());
			roleDto.setName(role.getName());
			roleDto.setDescription(role.getDescription());
			roles.add(roleDto);
		}
	}	

	@Data
	@Getter
	@Setter
	public static class Organization {
		private String id;
		private String name;
		private String description;
	}

	@Data
	@Getter
	@Setter
	public static class User {
		private String id;
		private String userName;
		private String firstName;
		private String lastName;
		private String email;
	}

	@Data
	@Getter
	@Setter
	public static class Role {
		private String id;
		private String name;
		private String description;
	}

}
