package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UnLinkRolesResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private UserResponse user;

	public UnLinkRolesResponse(User user) {
		this.user = new UserResponse(user);
	}
}
