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
public class ListUsersResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<UserResponse> users = new ArrayList<>();

	public ListUsersResponse(List<User> userEntities) {
		for (User u : userEntities) {
			UserResponse userDto = new UserResponse(u);
			users.add(userDto);
		}
	}

}
