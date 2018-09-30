package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateRoleRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;
	@JsonIgnore
	public Role getRole() {
		Role role = new Role();
		role.setName(this.name);
		role.setDescription(this.description);
		return role;
	}

}
