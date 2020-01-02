package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.certification.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateUserResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<UserResponse> users = new ArrayList<UserResponse>();

	public CreateUserResponse(List<User> userEntities) {
		for (User u : userEntities) {
			users.add(new UserResponse(u));
		}
	}

}
