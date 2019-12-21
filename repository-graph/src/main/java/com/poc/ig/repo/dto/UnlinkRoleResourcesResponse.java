package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UnlinkRoleResourcesResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private RoleResponse role;

	public UnlinkRoleResourcesResponse(Role roleEntity) {
		this.role = new RoleResponse(roleEntity);
	}
}
