package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateRoleResponse2 implements Serializable {
	private static final long serialVersionUID = 1L;
	private RoleResponse role;

	public CreateRoleResponse2(Role roleIn) {
		this.role = new RoleResponse(roleIn);
	}

}
