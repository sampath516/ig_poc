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
public class ListRolesResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	List<RoleResponse> roles = new ArrayList<>();

	public ListRolesResponse(List<Role> roleEntities) {
		for (Role r : roleEntities) {
			RoleResponse roleDto = new RoleResponse(r);
			roles.add(roleDto);
		}
	}

}
