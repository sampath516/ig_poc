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
public class UpdateResourceRequest2 implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;

	private Organization organization;
	private List<User> users = new ArrayList<>();
	private List<Role> roles = new ArrayList<>();
	
	public Resource getUpdatedRole(Resource resource, boolean basicUpdate) {
		if (basicUpdate) {
			resource.setId(id);
			resource.setName(name);
			resource.setDescription(description);
		}
		return resource;
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
	public static class Role {
		private long id;
		private String name;
		private String description;
	}
}
