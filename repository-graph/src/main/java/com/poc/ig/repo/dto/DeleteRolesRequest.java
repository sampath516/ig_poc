package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DeleteRolesRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> roles;

}
