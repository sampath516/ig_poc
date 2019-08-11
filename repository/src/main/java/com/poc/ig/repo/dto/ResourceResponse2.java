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
public class ResourceResponse2 implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;
	private String application;
	private String owner;

	private List<User> users = new ArrayList<>();
	private List<Role> roles = new ArrayList<>();
	
	
	public ResourceResponse2(Resource resource) {
		this.id = resource.getId();
		this.name = resource.getName();
		this.description = resource.getDescription();
		
		this.application = resource.getApplication().getName();
		this.owner = resource.getApplication().getOwner().getUserName();
		
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
	public static class Role {
		private long id;
		private String name;
		private String description;
	}

}
