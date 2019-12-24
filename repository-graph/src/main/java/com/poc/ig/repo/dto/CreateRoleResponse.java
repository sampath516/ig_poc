package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CreateRoleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<RoleResponse> roles = new ArrayList<RoleResponse>();

	public CreateRoleResponse(List<Role> rolesIn) {
		for(Role r: rolesIn) {
			roles.add(new RoleResponse(r));
		}
	}
}
