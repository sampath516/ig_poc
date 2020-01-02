package com.poc.ig.repo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserRequest {
	private String externalId;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String manager;
	private String organization;

}
