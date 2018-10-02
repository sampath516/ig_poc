package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateUserRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String organizationId;
	@JsonIgnore
	public User getUser() {
		User user = new User();
		user.setId(id);
		user.setUserName(userName);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		return user;
	}
}
