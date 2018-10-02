package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Organization organization;
	private List<Role> roles = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();

	public UserResponse(User user) {
		this.id = user.getId();
		this.userName = user.getUserName();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();

		this.organization = new Organization();
		this.organization.setId(user.getOrganization().getId());
		this.organization.setName(user.getOrganization().getName());
		this.organization.setDescription(user.getOrganization().getDescription());

		for (com.poc.ig.repo.entity.Role r : user.getRoles()) {
			Role roleDto = new Role();
			roleDto.setId(r.getId());
			roleDto.setName(r.getName());
			roleDto.setDescription(r.getDescription());
			this.roles.add(roleDto);
		}

		for (com.poc.ig.repo.entity.Resource res : user.getResources()) {
			Resource resDto = new Resource();
			resDto.setId(res.getId());
			resDto.setName(res.getName());
			resDto.setDescription(res.getDescription());
			this.resources.add(resDto);
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
	public static class Role {
		private long id;
		private String name;
		private String description;
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
