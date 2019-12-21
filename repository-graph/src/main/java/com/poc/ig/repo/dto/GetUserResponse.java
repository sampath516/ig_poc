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
public class GetUserResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private UserResponse user;
	@JsonIgnore
	public GetUserResponse(User user) {
		this.user = new UserResponse(user);
	}
}
