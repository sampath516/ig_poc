package com.poc.ig.certification.test.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class CreateRoleResponse {
	
	List<RoleResponse> roles = new ArrayList<RoleResponse>();

}