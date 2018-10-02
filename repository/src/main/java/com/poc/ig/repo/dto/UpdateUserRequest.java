package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateUserRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Organization organization;
	private List<Role> roles = new ArrayList<>();
	private List<Resource> resources = new ArrayList<>();
	@JsonIgnore
	public User getUpdatedUser(User user, boolean basicUpdate) {
		if (basicUpdate) {
			user.setUserName(this.userName);
			user.setFirstName(this.firstName);
			user.setLastName(this.lastName);
			user.setEmail(this.email);
		}
		return user;
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
