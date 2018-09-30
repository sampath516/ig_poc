package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateRoleResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private RoleResponse role;

	public UpdateRoleResponse(Role role) {
		this.role = new RoleResponse(role);
	}

}
