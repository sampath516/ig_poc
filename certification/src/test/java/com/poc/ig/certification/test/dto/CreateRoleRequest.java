package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateRoleRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	List<RoleDto> roles = new ArrayList<RoleDto>();

}
