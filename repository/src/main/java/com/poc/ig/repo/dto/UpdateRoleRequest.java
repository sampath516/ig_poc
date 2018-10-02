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
public class UpdateRoleRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;

	private Organization organization;
	private List<User> users = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();
	
	public Role getUpdatedRole(Role role, boolean basicUpdate) {
		if (basicUpdate) {
			role.setId(id);
			role.setName(name);
			role.setDescription(description);
		}
		return role;
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
